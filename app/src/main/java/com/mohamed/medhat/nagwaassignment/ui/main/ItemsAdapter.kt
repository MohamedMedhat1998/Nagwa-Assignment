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
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import javax.inject.Inject

/**
 * Populates the list of [DataItem]s to the ui.
 */
class ItemsAdapter @Inject constructor() :
    ListAdapter<DataItem, ItemsAdapter.DataItemHolder>(DataItemDiffUtilCallback) {

    var onDownloadClicked: (DataItem) -> Unit = {}
    var onCancelDownloadClicked: (DataItem) -> Unit = {}

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

        init {
            binding.ibDataItemAction.setOnClickListener {
                when (currentList[adapterPosition].state.state) {
                    DownloadState.STATE_DOWNLOADED -> {
                        when (currentList[adapterPosition].type) {
                            Constants.TYPE_VIDEO -> {
                                // TODO open the video in ExoPlayer.
                            }
                            Constants.TYPE_PDF -> {
                                // TODO preview the PDF.
                            }
                        }
                    }
                    DownloadState.STATE_DOWNLOADING -> {
                        onCancelDownloadClicked.invoke(currentList[adapterPosition])
                    }
                    DownloadState.STATE_NOT_DOWNLOADED -> {
                        onDownloadClicked.invoke(currentList[adapterPosition])
                    }
                }
            }

            binding.ibDataItemDelete.setOnClickListener {
                // TODO delete the media
            }
        }

        /**
         * Binds a single [DataItem] to the ui.
         */
        fun bind(dataItem: DataItem) {
            binding.tvDataItemName.text = dataItem.name
            when (dataItem.state.state) {
                DownloadState.STATE_DOWNLOADED -> {
                    binding.pbDataItemDownloadProgress.visibility = View.GONE
                    binding.tvDataItemDownloadPercentage.visibility = View.GONE
                    binding.ibDataItemDelete.visibility = View.VISIBLE
                    when (dataItem.type) {
                        Constants.TYPE_VIDEO -> {
                            Glide.with(binding.root.context)
                                .load(R.drawable.ic_play)
                                .into(binding.ibDataItemAction)
                        }
                        Constants.TYPE_PDF -> {
                            Glide.with(binding.root.context)
                                .load(R.drawable.ic_read)
                                .into(binding.ibDataItemAction)
                        }
                    }
                }
                DownloadState.STATE_DOWNLOADING -> {
                    binding.pbDataItemDownloadProgress.visibility = View.VISIBLE
                    binding.tvDataItemDownloadPercentage.visibility = View.VISIBLE
                    binding.ibDataItemDelete.visibility = View.GONE
                    binding.pbDataItemDownloadProgress.progress = dataItem.state.progress
                    binding.tvDataItemDownloadPercentage.text = binding.root.context.getString(
                        R.string.download_percentage,
                        dataItem.state.progress
                    )
                    Glide.with(binding.root.context)
                        .load(R.drawable.ic_cancel)
                        .into(binding.ibDataItemAction)
                }
                DownloadState.STATE_NOT_DOWNLOADED -> {
                    binding.pbDataItemDownloadProgress.visibility = View.GONE
                    binding.tvDataItemDownloadPercentage.visibility = View.GONE
                    binding.ibDataItemDelete.visibility = View.GONE
                    Glide.with(binding.root.context)
                        .load(R.drawable.ic_download)
                        .into(binding.ibDataItemAction)
                }
            }
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
     * Updates the state of the passed [dataItem] with the new state it has.
     * @param dataItem The [DataItem] that has a state updates.
     */
    fun updateItemState(dataItem: DataItem) {
        currentList.forEachIndexed { index, it ->
            if (it.id == dataItem.id) {
                it.state = dataItem.state
                notifyItemChanged(index)
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