package fr.ratp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.ratp.databinding.WordViewholderBinding

class WordAdapter :
    PagingDataAdapter<WordUiModel, WordAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            WordViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { tile ->
            holder.bind(tile)
        }
    }

    class ViewHolder(
        private val binding: WordViewholderBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(word: WordUiModel) {
            binding.apply {
                this.wordText.text = word.text
            }
        }
    }

    private companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WordUiModel>() {
            override fun areItemsTheSame(oldItem: WordUiModel, newItem: WordUiModel): Boolean =
                oldItem.text == newItem.text

            override fun areContentsTheSame(oldItem: WordUiModel, newItem: WordUiModel): Boolean =
                oldItem == newItem
        }
    }
}

