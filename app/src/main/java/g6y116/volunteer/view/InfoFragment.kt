package g6y116.volunteer.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import g6y116.volunteer.R
import g6y116.volunteer.databinding.FragmentInfoBinding
import g6y116.volunteer.onClick
import g6y116.volunteer.viewmodel.DetailViewModel
import androidx.navigation.fragment.findNavController

class InfoFragment : Fragment() {

    private val binding by lazy { FragmentInfoBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.btn.onClick {
            if (viewModel.url.isNotEmpty())
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url)))
        }

        binding.fab.onClick {
            findNavController().navigate(R.id.mapFragment)
        }
    }

    private fun setObserver() {

    }
}