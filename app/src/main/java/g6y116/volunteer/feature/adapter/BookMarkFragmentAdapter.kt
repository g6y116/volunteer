package g6y116.volunteer.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import g6y116.volunteer.databinding.ItemVolrunteerInfoBinding
import g6y116.volunteer.feature.data.model.MainUiState

class BookMarkFragmentAdapter(private val action: (MainUiState) -> Unit):
    ListAdapter<MainUiState, BookMarkFragmentAdapter.ViewHolder>(diff) {

    companion object {
        private val diff = object : DiffUtil.ItemCallback<MainUiState>() {
            override fun areItemsTheSame(old: MainUiState, new: MainUiState) = old.pID == new.pID
            override fun areContentsTheSame(old: MainUiState, new: MainUiState) = old == new
        }
    }

    inner class ViewHolder(
        private val binding: ItemVolrunteerInfoBinding,
        private val action: (MainUiState) -> Unit,
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MainUiState) {
            binding.item = item
            binding.bookmarkIv.visibility = View.GONE
            binding.labelV.visibility = View.GONE
            binding.root.setOnClickListener {
                action.invoke(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.run { holder.bind(this) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkFragmentAdapter.ViewHolder {
        val binding = ItemVolrunteerInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, action)
    }
}