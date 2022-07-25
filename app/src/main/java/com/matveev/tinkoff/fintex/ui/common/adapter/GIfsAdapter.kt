package com.matveev.tinkoff.fintex.ui.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.matveev.tinkoff.fintex.R
import com.matveev.tinkoff.fintex.data.model.GifsResult
import com.matveev.tinkoff.fintex.databinding.ItemCategoriesBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GIfsAdapter (
    private var results: ArrayList<GifsResult>,
) : RecyclerView.Adapter<GIfsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(results[position])

    override fun getItemCount() = results.size

    class ViewHolder(private val binding: ItemCategoriesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(results: GifsResult) {
            binding.apply {

                Glide
                    .with(itemView.context)
                    .asGif()
                    .load(results.url.replace("http", "https"))
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
                                descriptionText.text = results.description
                            }
                            return false
                        }

                    })
                    .error(R.drawable.error_photo)
                    .into(imageGif)
            }
        }
    }
}