package com.devs.imgur.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devs.imgur.R
import com.devs.imgur.data.repository.modal.Data
import com.devs.imgur.data.repository.resource.Status
import com.devs.imgur.databinding.FragmentSearchBinding
import com.devs.imgur.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private val viewModel by viewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageListAdapter: ImageListAdapter
    private var imageList = mutableListOf<Data>()
    private var isViewTypeList = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        viewModel.imageState().observe(viewLifecycleOwner, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showLoader()
                }
                Status.SUCCESS -> {
                    hideLoader()
                    resource.data?.let {
                        if(it.data.isNullOrEmpty().not()) {
                            imageList.clear()
                            it.data?.let { data -> imageList.addAll(data) }
                            imageListAdapter.notifyDataSetChanged()
                        } else {
                            toast("No Result Found")
                        }
                    }
                }
                Status.ERROR -> {
                    hideLoader()
                    Timber.e("Error: " + errorMessage(resource))
                }
                else -> {
                    hideLoader()
                }
            }
        })
    }

    private fun init() {
        binding.toolbar.inflateMenu(R.menu.search_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_list_grid_toggle) {
                hideKeyboard(requireActivity())
                toggleViewType(menuItem)
            }
            false
        }
        binding.btnSearch.setOnClickListener {
            if(binding.simpleSearchView.query.isNotEmpty()) {
                viewModel.searchImage(binding.simpleSearchView.query.toString())
                binding.simpleSearchView.clearFocus()
            }
        }

        binding.simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchImage(query.toString())
                binding.simpleSearchView.clearFocus()
                return false
            }
        })
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        imageListAdapter = ImageListAdapter(imageList)
        binding.recyclerView.adapter = imageListAdapter
        imageListAdapter.onItemClicked = {
            // Perform on item click
        }
    }

    fun toggleViewType(menuItem: MenuItem) {
        isViewTypeList = imageListAdapter.toggleItemViewType()
        binding.recyclerView.setLayoutManager(
            if (isViewTypeList) LinearLayoutManager(requireContext())
            else GridLayoutManager(requireContext(), 2)
        )
        if (isViewTypeList) {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_grid_view_24
            )?.let {
                menuItem.icon = it
            }
            // Show ListView
            binding.recyclerView.setLayoutManager(
                LinearLayoutManager(requireContext())
            )
        } else {
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_view_list_24
            )?.let {
                menuItem.icon = it
            }
            // Show GridView
            binding.recyclerView.setLayoutManager(
                GridLayoutManager(requireContext(), 2)
            )
        }
        imageListAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }

    companion object {
        fun newInstance() = SearchFragment()
    }

}