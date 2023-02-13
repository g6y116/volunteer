package g6y116.volunteer.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import g6y116.volunteer.Const
import g6y116.volunteer.R
import g6y116.volunteer.adapter.BookMarkAdapter
import g6y116.volunteer.adapter.ViewHolderBindListener
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.databinding.FragmentBookMarkBinding
import g6y116.volunteer.onClick
import g6y116.volunteer.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class BookMarkFragment : Fragment(), ViewHolderBindListener {

    private val binding by lazy { FragmentBookMarkBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()
    private val adapter: BookMarkAdapter = BookMarkAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.adapter = adapter

        lifecycleScope.launch {
            viewModel.bookMarkList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.menu_2).toString())
        }
    }

    override fun onViewHolderBind(holder: RecyclerView.ViewHolder, item: Any) {
        val item = item as VolunteerInfo

//        holder.itemView.findViewById<ImageView>(R.id.ivBookMark).visibility = if (item.isBookMark) View.VISIBLE else View.GONE
        holder.itemView.onClick {
            lifecycleScope.launch {
                startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra("pID", item.pID)
                    putExtra("url", item.url)
                    putExtra("from", Const.BOOK_MARK)
                })
            }
        }
    }
}