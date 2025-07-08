package com.example.madcamp1.ui.tab1

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp1.data.ProblemListItem
import com.example.madcamp1.databinding.ItemHeaderBinding
import com.example.madcamp1.databinding.ItemProblemBinding
import androidx.core.content.ContextCompat
import com.example.madcamp1.R


class ProblemListAdapter(
    private var items: List<ProblemListItem>,
    private val onHeaderClick: ((ProblemListItem.Header) -> Unit)? = null,
    private val onProblemClickListener: ((ProblemListItem.Item) -> Unit)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ProblemListItem.Header -> TYPE_HEADER
            is ProblemListItem.Item -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding = ItemProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("RecyclerView", "onBindViewHolder position=$position, item=${items[position]}")
        when (val item = items[position]) {
            is ProblemListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ProblemListItem.Item -> {
                val itemHolder = holder as ItemViewHolder
                itemHolder.bind(item)

                // 배경색 설정 - 번갈아 적용
                val bgColor = if (position % 2 == 0)
                    ContextCompat.getColor(holder.itemView.context, R.color.light_gray)
                else
                    ContextCompat.getColor(holder.itemView.context, R.color.white)

                holder.itemView.setBackgroundColor(bgColor)
            }
        }
    }

    fun updateList(newList: List<ProblemListItem>) {
        items = newList
        notifyDataSetChanged()
    }


    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(header: ProblemListItem.Header) {
            binding.textViewHeader.text = header.title


            binding.textArrow.text = if (header.isExpanded) "▼" else "▶"

            binding.root.setOnClickListener {
                onHeaderClick?.invoke(header)
            }
        }
    }


    inner class ItemViewHolder(private val binding: ItemProblemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProblemListItem.Item) {
            binding.textProblem.text = item.name
            binding.root.setOnClickListener {
                onProblemClickListener?.invoke(item)
            }
        }
    }


}
