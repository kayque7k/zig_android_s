package com.example.comics.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comics.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.comics.view.ViewModel.Event.SetupList
import com.example.comics.view.ViewModel.Event.Error
import com.example.comics.view.ViewModel.Event.LoadingIndicator
import com.example.comics.view.ViewModel.Event.CloseIndicator
import com.example.comics.view.ViewModel.Event.Empty
import com.example.comics.view.ViewModel.Event.UpdateList

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModel()
    private lateinit var adapter: Adapter

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        observer()

        viewModel.getComics()

        swipeList()
    }

    private fun observer() = lifecycleScope.launch {
        viewModel.event = { event ->
            when (event) {
                is SetupList -> {
                    adapter = Adapter(event.item.toMutableList())
                    viewList()
                }

                is UpdateList -> {
                    updateViewList(event.item, event.countUpdate)
                }

                is Error -> error()
                is LoadingIndicator -> showLoading()
                is CloseIndicator -> closeLoading()
                is Empty -> empty()
            }
        }
    }

    private fun swipeList() = with(binding?.swipeRefresh) {
        this?.setOnRefreshListener {
            viewModel.getComics()
        }
    }

    private fun viewList() {
        with(binding) {
            this?.errorTV?.visibility = View.GONE
            this?.listItem?.visibility = View.VISIBLE
            this?.listItem?.adapter = adapter
            this?.listItem?.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateViewList(list: List<ItemVO>, count: Int) {
        with(binding) {
            this?.errorTV?.visibility = View.GONE
            this?.listItem?.visibility = View.VISIBLE
            this?.counterTV?.text = count.toString()
            adapter.update(list)
        }
    }

    private fun error() {
        with(binding) {
            this?.listItem?.visibility = View.GONE
            this?.errorTV?.visibility = View.VISIBLE
        }
    }

    private fun empty() {
        with(binding) {
            this?.listItem?.visibility = View.GONE
            this?.errorTV?.visibility = View.GONE
        }
    }

    private fun showLoading() = with(binding) {
        this?.swipeRefresh?.isRefreshing = true
    }

    private fun closeLoading() = with(binding) {
        this?.swipeRefresh?.isRefreshing = false
    }
}