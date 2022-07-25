package com.matveev.tinkoff.fintex.ui.features.last.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.matveev.tinkoff.fintex.data.GifsCategoryNames
import com.matveev.tinkoff.fintex.data.api.categories.GifsRetrofitBuilder
import com.matveev.tinkoff.fintex.data.networkerror.RequestError
import com.matveev.tinkoff.fintex.data.repository.GifsRepository
import com.matveev.tinkoff.fintex.databinding.FragmentLastBinding
import com.matveev.tinkoff.fintex.ui.ViewState.Success
import com.matveev.tinkoff.fintex.ui.ViewState.Error
import com.matveev.tinkoff.fintex.ui.ViewState.Loading
import com.matveev.tinkoff.fintex.ui.common.adapter.GIfsAdapter
import com.matveev.tinkoff.fintex.ui.common.viewmodel.GifViewModel
import com.matveev.tinkoff.fintex.ui.common.viewmodel.GifViewModelFactory
import com.matveev.tinkoff.fintex.utils.toast
import java.net.UnknownHostException


class LastGifFragment : Fragment() {

    private var _binding: FragmentLastBinding? = null
    private val binding get() = _binding!!

    private lateinit var gifViewModel: GifViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupCategoriesObserver()

        binding.apply {

            repeatNoInternetText.setOnClickListener {
                load()
            }

            repeatEmptyResultText.setOnClickListener {
                load()
            }

            newGifFab.setOnClickListener {
                load()
            }
        }
    }

    private fun load(){
        gifViewModel.load(
            categoriesName = GifsCategoryNames.LAST,
            page = (0..2000).random()
        )
    }

    private fun setupViewModel() {
        gifViewModel = ViewModelProvider(
            this,
            GifViewModelFactory(
                GifsRepository(GifsRetrofitBuilder.gifsApiService),
                categoriesName = GifsCategoryNames.LAST,
                page = (0..2000).random()
            )
        ).get(GifViewModel::class.java)
    }

    private fun setupCategoriesObserver() {
        gifViewModel.categoriesGifLiveData.observe(viewLifecycleOwner, { results ->
            when (results) {
                is Success -> {
                    binding.progressBar.isVisible = false

                    if (results.data?.result.isNullOrEmpty()) {
                        binding.apply {
                            categoriesRecycler.isVisible = false
                            errorWithEmptyResultLinear.isVisible = true
                            binding.newGifFab.isVisible = false
                        }
                    } else {
                        binding.categoriesRecycler.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = results.data?.result?.let { GIfsAdapter(it) }
                            binding.newGifFab.isVisible = true
                        }
                    }
                }
                is Loading -> {
                    binding.apply {
                        binding.newGifFab.isVisible = true
                        errorNoInternetLinear.isVisible = false
                        errorWithEmptyResultLinear.isVisible = false
                        categoriesRecycler.isVisible = true
                        progressBar.isVisible = true
                    }
                }
                is Error -> {
                    binding.newGifFab.isVisible = false
                    binding.progressBar.isVisible = false

                    val msg = RequestError.getErrorMessage(results.exception)

                    when (results.exception) {
                        is UnknownHostException -> {
                            binding.apply {
                                categoriesRecycler.isVisible = false
                                errorNoInternetLinear.isVisible = true
                                errorWithEmptyResultLinear.isVisible = false
                            }
                        }
                        else -> requireContext().toast(msg)
                    }
                }
            }
        })
    }
}
