package com.example.newsapi.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.db.ArticleDatabase
import com.example.newsapi.repository.HeadlinesRepository
import com.example.newsapi.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapi.util.Resource
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.viewmodels.MainViewModelFactory
import com.example.newsapi.views.MainActivity

class BreakingNewsFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadineAdapter
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val view = inflater.inflate(R.layout.fragment_breaking_news, container, false)
        recyclerView = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        setUpRecyclerView()
        setUpViewModel()
        adapter.setOnItenClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        mainViewModel.bNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgreesBar()
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(
                            newsResponse.articles.toList().sortedByDescending { it.publishedAt }
                        )
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = mainViewModel.breakingNewsPageNumber == totalPages
                        if (isLastPage) {
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgreesBar()
                    response.message.let {
                        Toast.makeText(activity, "Error: " + it, Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgreeBar()
                }
            }
        }

        return view
    }

    private fun showProgreeBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgreesBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = HeadineAdapter(activity as MainActivity)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(this.scrollListener)
    }

    private fun setUpViewModel() {
        val repository =
            HeadlinesRepository(ArticleDatabase(activity as MainActivity))
        mainViewModel = ViewModelProvider(
            requireActivity(),
            MainViewModelFactory((activity as MainActivity).application, repository)
        ).get(MainViewModel::class.java)
    }

    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingandNotonLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingandNotonLastPage && isAtLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                mainViewModel.getbreakingNews("in")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

}