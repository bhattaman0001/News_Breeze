package com.example.newsapi.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

@Suppress("COMPATIBILITY_WARNING")
class BreakingNewsFragment : Fragment() {
    lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HeadineAdapter
    private lateinit var progressBar: ProgressBar
    private var sort: MenuItem? = null
    private var unSort: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_breaking_news, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar_top)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
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
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        adapter.differ.submitList(
                            newsResponse.articles.toList()
                        )
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = mainViewModel.breakingNewsPageNumber == totalPages
                        if (isLastPage) {
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let {
                        Toast.makeText(activity, "Error: $it", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        return view
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
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

    private val scrollListener = object : RecyclerView.OnScrollListener() {

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
                mainViewModel.getBreakingNews("in")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.dot_menu, menu)
        sort = menu.findItem(R.id.sort)
        unSort = menu.findItem(R.id.unSort)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                mainViewModel.bNews.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { newsResponse ->
                                adapter.differ.submitList(
                                    newsResponse.articles.toList().sortedBy { it.publishedAt }
                                )
                                val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                                isLastPage = mainViewModel.breakingNewsPageNumber == totalPages
                                if (isLastPage) {
                                    recyclerView.setPadding(0, 0, 0, 0)
                                }
                            }
                            // disabling the sort icon after clicking it and enabling the un-sort icon
                            sort?.setVisible(false)
                            unSort?.setVisible(true)
                            view?.let {
                                Snackbar.make(it, "News is Sorted", Snackbar.LENGTH_SHORT).show()
                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message.let {
                                Toast.makeText(activity, "Error: $it", Toast.LENGTH_LONG).show()
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }

                }
                return true
            }

            R.id.unSort -> {
                mainViewModel.bNews.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let { newsResponse ->
                                adapter.differ.submitList(
                                    newsResponse.articles.toList()
                                )
                                val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                                isLastPage = mainViewModel.breakingNewsPageNumber == totalPages
                                if (isLastPage) {
                                    recyclerView.setPadding(0, 0, 0, 0)
                                }
                            }
                            // enabling the sort icon after clicking it and disabling the un-sort icon
                            sort?.setVisible(true)
                            unSort?.setVisible(false)
                            view?.let {
                                Snackbar.make(it, "News is UnSorted", Snackbar.LENGTH_SHORT).show()
                            }
                        }

                        is Resource.Error -> {
                            hideProgressBar()
                            response.message.let {
                                Toast.makeText(activity, "Error: $it", Toast.LENGTH_LONG).show()
                            }
                        }

                        is Resource.Loading -> {
                            showProgressBar()
                        }
                    }

                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

}