package com.example.newsapi.views.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.util.Resource
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.views.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadineAdapter
    lateinit var progressBar: ProgressBar
    lateinit var etSearch: EditText
    lateinit var layout: RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvSearchNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        etSearch = view.findViewById(R.id.etSearch)
        layout = view.findViewById(R.id.emptySearchArticle)
        val bt_clear = view.findViewById<View>(R.id.bt_clear) as ImageButton

        bt_clear.setOnClickListener {
            etSearch.setText("")
        }

        setUpRecyclerView()
        setUpViewModel()

        adapter.setOnItenClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                searchAction()
                return@OnEditorActionListener true
            }
            false
        })

        mainViewModel.searchnews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgreesBar()
                    response.data?.let { newsResponse ->

                        if (newsResponse.articles.isEmpty()) {
                            recyclerView.visibility = View.INVISIBLE
                            layout.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            layout.visibility = View.INVISIBLE
                        }
                        adapter.differ.submitList(newsResponse.articles)

                    }
                }

                is Resource.Error -> {
                    hideProgreesBar()
                    response.message.let {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgreeBar()
                }

            }

        })
    }

    private fun hideKeyboard() {
        val view: View? = (activity as MainActivity).currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

    private fun searchAction() {

        val query: String = etSearch.getText().toString().trim { it <= ' ' }
        var job: Job? = null
        if (query != "") {
            job?.cancel()
            job = MainScope().launch {
                mainViewModel.searchNews(query)
            }
        } else {
            Toast.makeText(activity as MainActivity, "Please fill search input", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun showProgreeBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgreesBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HeadineAdapter(activity as MainActivity)
        recyclerView.adapter = adapter
    }

    private fun setUpViewModel() {
        mainViewModel = (activity as MainActivity).mainViewModel
    }
}