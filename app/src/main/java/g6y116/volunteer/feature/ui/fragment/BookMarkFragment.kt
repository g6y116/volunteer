package g6y116.volunteer.feature.ui.fragment

import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import g6y116.volunteer.R
import g6y116.volunteer.base.abstracts.BaseFragment
import g6y116.volunteer.feature.adapter.BookMarkFragmentAdapter
import g6y116.volunteer.base.utils.toast
import g6y116.volunteer.databinding.FragmentBookMarkBinding
import g6y116.volunteer.feature.ui.activity.DetailActivity
import g6y116.volunteer.feature.ui.activity.MainActivity
import g6y116.volunteer.feature.ui.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookMarkFragment : BaseFragment<FragmentBookMarkBinding>(R.layout.fragment_book_mark) {

    override val viewModel: MainViewModel by activityViewModels()
    private val adapter: BookMarkFragmentAdapter = BookMarkFragmentAdapter { item ->
        viewModel.clickVolunteer(
            item,
            { startActivity(Intent(context, DetailActivity::class.java).apply { putExtra("item", it) }) },
            {
                lifecycleScope.launch(Dispatchers.Main) {
                    toast(requireActivity(), getString(R.string.msg_volunteer_deleted))
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.toolbar_bookmark).toString())
        }
    }

    override fun initView() {
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.rv.itemAnimator = null
    }

    override fun setOnclick() {}

    override fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bookMarkUiState.collectLatest {
                    adapter.submitList(it)

                    binding.rv.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
                    binding.noResultL.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }
}