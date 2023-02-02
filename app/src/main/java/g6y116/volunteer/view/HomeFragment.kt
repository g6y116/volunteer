package g6y116.volunteer.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import g6y116.volunteer.Const
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

    private lateinit var activityContext: MainActivity
    private val adapter: HomeAdapter = HomeAdapter(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.adapter = adapter

        lifecycleScope.launch {
            viewModel.homeList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onViewHolderBind(holder: ViewHolder, item: Any) {
        val item = item as VolunteerInfo

        // 북마크 표시 기능 추가

        holder.itemView.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val volunteer = viewModel.getVolunteerDetail(item.pID)
                    startActivity(Intent(context, DetailActivity::class.java).apply {
                        putExtra("volunteer", volunteer)
                        putExtra("url", item.url)
                        putExtra("from", Const.HOME)
                    })
                } catch (e: Exception) {
                    // 토스트 뛰우기
                }
            }
        }
    }
}