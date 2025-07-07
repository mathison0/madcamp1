package com.example.madcamp1.ui.tab2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1.data.ContestAlbum
import com.example.madcamp1.databinding.ItemContestAlbumBinding

class ContestAlbumAdapter(
    private val items: List<ContestAlbum>,
    private val onClick: (ContestAlbum) -> Unit
) : RecyclerView.Adapter<ContestAlbumAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemContestAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ContestAlbum) {
            binding.title.text = item.title
            binding.thumbnail.setImageResource(item.thumbnail)
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContestAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}