package com.matveev.tinkoff.fintex.ui.features.random.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.matveev.tinkoff.fintex.R
import com.matveev.tinkoff.fintex.data.api.random.RandomGifRetrofitBuilder
import com.matveev.tinkoff.fintex.data.networkerror.RequestError
import com.matveev.tinkoff.fintex.data.repository.RandomGifRepository
import com.matveev.tinkoff.fintex.databinding.FragmentRandomBinding
import com.matveev.tinkoff.fintex.ui.ViewState.*
import com.matveev.tinkoff.fintex.ui.features.random.viewmodel.RandomGifViewModel
import com.matveev.tinkoff.fintex.ui.features.random.viewmodel.RandomGifViewModelFactory
import com.matveev.tinkoff.fintex.utils.toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.net.UnknownHostException


class RandomGifFragment : Fragment() {

    private var _binding: FragmentRandomBinding? = null
    private val binding get() = _binding!!

    private lateinit var randomGifViewModel: RandomGifViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupGifObserver()
        setupBackAllowedObserver()
        binding.apply {
            newGifFab.setOnClickListener {
                randomGifViewModel.nextGif()
            }

            lastGifFab.setOnClickListener {
                randomGifViewModel.previousGif()
            }

            repeatText.setOnClickListener {
                randomGifViewModel.load()
            }
        }
    }

    private fun setupViewModel() {
        randomGifViewModel = ViewModelProvider(
            this,
            RandomGifViewModelFactory(RandomGifRepository(RandomGifRetrofitBuilder.randomGifApiService))
        ).get(RandomGifViewModel::class.java)
    }

    private fun setupGifObserver() {
        randomGifViewModel.gifLiveData.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val url = (result.data?.url)?.replace("http", "https")
                    url?.let {
                        showData(url, result.data.description)
                    }
                }
                is Loading -> {
                    binding.apply {
                        descriptionText.text = ""
                        progressBar.isVisible = true
                        errorWithInternetLinear.isVisible = false
                        cardView.isVisible = true
                        linearFab.isVisible = true
                    }
                }
                is Error -> {
                    val msg = RequestError.getErrorMessage(result.exception)

                    when (result.exception) {
                        is UnknownHostException -> {
                            binding.apply {
                                cardView.isVisible = false
                                linearFab.isVisible = false
                                progressBar.isVisible = false
                                errorWithInternetLinear.isVisible = true
                            }
                        }
                        is NullPointerException -> {
                            binding.apply {
                                imageGif.setImageResource(R.drawable.error_photo)
                                progressBar.isVisible = false
                            }
                            requireContext().toast(msg)
                        }
                        else -> requireContext().toast(msg)

                    }
                }
            }
        })
    }

    private fun setupBackAllowedObserver() {
        randomGifViewModel.backAllowedLiveData.observe(viewLifecycleOwner, { allowed ->
            if (!allowed) {
                binding.apply {
                    lastGifFab.alpha = 1.0f
                    lastGifFab.animate()
                        .alpha(0.5f)
                        .duration = 300
                }
            } else {
                binding.apply {
                    val currentAlpha = lastGifFab.alpha.toString()
                    if (currentAlpha == "0.5") {
                        lastGifFab.alpha = 0.5f
                        lastGifFab.animate()
                            .alpha(1.0f)
                            .duration = 300
                    }
                }
            }
        })
    }

    private fun showData(url: String, description: String) {
        Glide
            .with(requireContext())
            .asGif()
            .load(url)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.apply {
                        progressBar.isVisible = false
                        descriptionText.text = description
                    }
                    return false
                }

            })
            .error(R.drawable.error_photo)
            .into(binding.imageGif)
    }

}