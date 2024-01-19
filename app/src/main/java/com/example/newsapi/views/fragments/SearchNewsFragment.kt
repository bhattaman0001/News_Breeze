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
import com.example.newsapi.adapters.HeadlineAdapter
import com.example.newsapi.util.Resource
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.views.MainActivity
import kotlinx.coroutines.Job

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadlineAdapter
    lateinit var progressBar: ProgressBar
    lateinit var etSearch: EditText
    lateinit var layout: RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvSearchNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        etSearch = view.findViewById(R.id.etSearch)
        layout = view.findViewById(R.id.emptySearchArticle)
        val btClear = view.findViewById<View>(R.id.bt_clear) as ImageButton

        btClear.setOnClickListener {
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

        mainViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
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
                    hideProgressBar()
                    response.message.let {
                        Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
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

        val query: String = etSearch.text.toString().trim { it <= ' ' }
        val job: Job? = null
        if (query != "") {
            job?.cancel()
        } else {
            Toast.makeText(activity as MainActivity, "Please fill search input", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HeadlineAdapter(activity as MainActivity)
        recyclerView.adapter = adapter
    }

    private fun setUpViewModel() {
        mainViewModel = (activity as MainActivity).mainViewModel
    }
}