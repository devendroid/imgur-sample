package com.devs.imgur.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devs.imgur.data.repository.modal.Data
import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.databinding.ItemImageGridBinding
import com.devs.imgur.databinding.ItemImageListBinding
import com.devs.imgur.util.toDDMMYYhhmmaa

internal typealias OnItemClicked = (data: Gallery) -> Unit

internal class ImageListAdapter(
    private var mData: List<Data>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val LIST_ITEM = 0
    private val GRID_ITEM = 1
    private var isViewTypeList = true;

    var onItemClicked: OnItemClicked? = null

    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        this.context = parent.context
        return if (viewType == LIST_ITEM) {
            ListItemViewHolder(
                ItemImageListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            GridItemViewHolder(
                ItemImageGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ListItemViewHolder) {
            viewHolder.bind(mData[position])
        }
        else if (viewHolder is GridItemViewHolder) {
            viewHolder.bind(mData[position])
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        if (isViewTypeList) {
            return LIST_ITEM
        } else {
            return GRID_ITEM
        }
    }

    fun toggleItemViewType(): Boolean {
        isViewTypeList = !isViewTypeList
        return isViewTypeList
    }
}

class ListItemViewHolder(private val binding: ItemImageListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Data) {
        binding.tvTitle.text = data.title
        binding.iv.load(data.images?.firstOrNull()?.link)
        binding.tvYear.text = (data.datetime * 1000).toDDMMYYhhmmaa()
        val imgCount = data.imagesCount?.minus(1)?:0
        binding.tvCount.text = if( imgCount > 0 ) "+$imgCount" else ""
    }
}

class GridItemViewHolder(private val binding: ItemImageGridBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Data) {
        binding.tvTitle.text = data.title
        binding.iv.load(data.images?.firstOrNull()?.link)
        binding.tvYear.text = (data.datetime * 1000).toDDMMYYhhmmaa()
        val imgCount = data.imagesCount?.minus(1)?:0
        binding.tvCount.text = if( imgCount > 0 ) "+$imgCount" else ""
    }
}