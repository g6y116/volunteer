package g6y116.volunteer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import g6y116.volunteer.databinding.FragmentMoreBinding
import g6y116.volunteer.viewmodel.MainViewModel

class MoreFragment : Fragment() {

    private val binding by lazy { FragmentMoreBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }
}