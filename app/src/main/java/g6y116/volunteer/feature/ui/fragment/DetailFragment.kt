package g6y116.volunteer.feature.ui.fragment

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import g6y116.volunteer.R
import g6y116.volunteer.feature.ui.viewmodel.DetailViewModel
import androidx.navigation.fragment.findNavController
import g6y116.volunteer.base.abstracts.BaseFragment
import g6y116.volunteer.base.utils.onClick
import g6y116.volunteer.databinding.FragmentDetailBinding
import g6y116.volunteer.feature.ui.activity.DetailActivity

class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    override val viewModel: DetailViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        activity?.let { detailActivity ->
            (detailActivity as DetailActivity).setToolbarTitle(getText(R.string.toolbar_detail).toString())
            (detailActivity as DetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun initView() {
        binding.viewmodel = viewModel
    }

    override fun setOnclick() {
        binding.btn.onClick {
            viewModel.detailUiState.value?.url?.let {
                if (it.isNotEmpty()) startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }

        binding.fab.onClick {
            findNavController().popBackStack()
            findNavController().navigate(R.id.mapFragment)
        }
    }

    override fun setObserver() {}
}