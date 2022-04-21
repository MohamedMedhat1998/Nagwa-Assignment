package com.mohamed.medhat.nagwaassignment.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohamed.medhat.nagwaassignment.R
import com.mohamed.medhat.nagwaassignment.databinding.ItemDataItemBinding
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.Constants
import javax.inject.Inject

/**
 * Populates the list of [DataItem]s to the ui.
 */
class ItemsAdapter @Inject constructor() :
    ListAdapter<DataItem, ItemsAdapter.DataItemHolder>(DataItemDiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_data_item, parent, false)
        return DataItemHolder(view)
    }

    override fun onBindViewHolder(holder: DataItemHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class DataItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemDataItemBinding.bind(itemView);

        /**
         * Binds a single [DataItem] to the ui.
         */
        fun bind(dataItem: DataItem) {
            // TODO handle the action button state and action.
            // TODO find a way to keep track with the state of the file (downloading, not downloaded, downloaded).
            // TODO handle the visibility of the download progress bar.
            binding.tvDataItemName.text = dataItem.name
            when (dataItem.type) {
                Constants.TYPE_VIDEO -> {
                    Glide.with(binding.root.context)
                        .load(R.drawable.ic_video)
                        .into(binding.ivDataItemIcon)
                }
                Constants.TYPE_PDF -> {
                    Glide.with(binding.root.context)
                        .load(R.drawable.ic_pdf)
                        .into(binding.ivDataItemIcon)
                }
            }
        }
    }

    /**
     * Defines whether two [DataItem]s are the same or not and whether the content is the same or not.
     */
    object DataItemDiffUtilCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.name == newItem.name && oldItem.type == newItem.type
        }

    }
}