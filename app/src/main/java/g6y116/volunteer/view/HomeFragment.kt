package g6y116.volunteer.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import g6y116.volunteer.R
import g6y116.volunteer.adapter.HomeAdapter
import g6y116.volunteer.adapter.ViewHolderBindListener
import g6y116.volunteer.databinding.FragmentHomeBinding
import g6y116.volunteer.repository.VolunteerInfo
import g6y116.volunteer.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), ViewHolderBindListener {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()

    private val adapter: HomeAdapter = HomeAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rv.layoutManager = LinearLayoutManager(requireActivity())
        binding.rv.adapter = adapter

        lifecycleScope.launch {
            viewModel.homeList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onViewHolderBind(holder: ViewHolder, item: Any) {
        val item = item as VolunteerInfo
//        Log.d("성준", "item : ${item}")
        holder.itemView.findViewById<TextView>(R.id.tvHost).text = item.host
        holder.itemView.findViewById<TextView>(R.id.tvTitle).text = item.title
        holder.itemView.setOnClickListener {
            // 이동
        }
    }
}