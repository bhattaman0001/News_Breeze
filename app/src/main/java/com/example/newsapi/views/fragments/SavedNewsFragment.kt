package com.example.newsapi.views.fragments

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.adapters.HeadineAdapter
import com.example.newsapi.viewmodels.MainViewModel
import com.example.newsapi.views.MainActivity
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var mainViewModel: MainViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: HeadineAdapter
    lateinit var layout: RelativeLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvSavedNews)
        layout = view.findViewById(R.id.emptyLayout)

        setUpRecyclerView()
        setUpViewModel()

        adapter.setOnItenClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )

        }

        val itemtouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = adapter.differ.currentList[position]
                mainViewModel.deleteArticle(article)
                Snackbar.make(view, "Sucessfully Deleted article", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        mainViewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemtouchHelperCallback).apply {
            attachToRecyclerView(recyclerView)
        }
        mainViewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isEmpty()) {
                recyclerView.visibility = View.INVISIBLE
                layout.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                layout.visibility = View.INVISIBLE
            }
            adapter.differ.submitList(articles)

        })
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