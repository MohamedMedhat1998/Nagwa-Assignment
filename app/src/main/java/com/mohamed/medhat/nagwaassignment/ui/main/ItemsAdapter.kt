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
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState
import javax.inject.Inject

/**
 * Populates the list of [DataItem]s to the ui.
 */
class ItemsAdapter @Inject constructor() :
    ListAdapter<DataItem, ItemsAdapter.DataItemHolder>(DataItemDiffUtilCallback) {

    var onDownloadClicked: (DataItem) -> Unit = {}
    var onCancelDownloadClicked: (DataItem) -> Unit = {}
    var onDeleteMediaClicked: (DataItem) -> Unit = {}
    var onDisplayClicked: (DataItem) -> Unit = {}

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
                        onDisplayClicked.invoke(currentList[adapterPosition])
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
                onDeleteMediaClicked.invoke(currentList[adapterPosition])
            }
        }

        /**
         * Binds a single [DataItem] to the ui.
         */
        fun bind(dataItem: DataItem) {
            binding.tvDataItemName.text = dataItem.name
            Glide.with(binding.root.context)
                .load(dataItem.getMediaLogo())
                .into(binding.ivDataItemIcon)

            when (dataItem.state.state) {
                DownloadState.STATE_DOWNLOADED -> {
                    binding.pbDataItemDownloadProgress.visibility = View.GONE
                    binding.tvDataItemDownloadPercentage.visibility = View.GONE
                    binding.ibDataItemDelete.visibility = View.VISIBLE
                    Glide.with(binding.root.context)
                        .load(dataItem.getActionButtonIcon())
                        .into(binding.ibDataItemAction)

                }
                DownloadState.STATE_DOWNLOADING -> {
                    binding.pbDataItemDownloadProgress.visibility = View.VISIBLE
                    binding.ibDataItemDelete.visibility = View.GONE
                    Glide.with(binding.root.context)
                        .load(R.drawable.ic_cancel)
                        .into(binding.ibDataItemAction)

                    if (dataItem.state.progress == -1) {
                        binding.tvDataItemDownloadPercentage.visibility = View.GONE
                        binding.pbDataItemDownloadProgress.isIndeterminate = true
                    } else {
                        binding.tvDataItemDownloadPercentage.visibility = View.VISIBLE
                        binding.pbDataItemDownloadProgress.isIndeterminate = false
                        binding.pbDataItemDownloadProgress.progress = dataItem.state.progress
                        binding.tvDataItemDownloadPercentage.text = binding.root.context.getString(
                            R.string.download_percentage,
                            dataItem.state.progress
                        )
                    }
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