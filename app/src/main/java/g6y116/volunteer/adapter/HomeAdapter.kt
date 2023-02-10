package g6y116.volunteer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.databinding.ItemVolrunteerInfoBinding

class HomeAdapter(private val viewHolderBindListener: ViewHolderBindListener):
    PagingDataAdapter<VolunteerInfo, HomeAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<VolunteerInfo>() {
            override fun areItemsTheSame(old: VolunteerInfo, new: VolunteerInfo) = old.pID == new.pID
            override fun areContentsTheSame(old: VolunteerInfo, new: VolunteerInfo) = old == new
        }
    }

    inner class ViewHolder(
        private val binding: ItemVolrunteerInfoBinding,
        private val listener: ViewHolderBindListener
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VolunteerInfo) {
            binding.item = item
            listener.onViewHolderBind(this, item)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.run { holder.bind(this) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVolrunteerInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewHolderBindListener)
    }
}