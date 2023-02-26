package g6y116.volunteer.view

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import g6y116.volunteer.Const
import g6y116.volunteer.R
import g6y116.volunteer.databinding.FragmentMoreBinding
import g6y116.volunteer.onClick
import g6y116.volunteer.viewmodel.MainViewModel

class MoreFragment : Fragment() {

    private val binding by lazy { FragmentMoreBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mainActivity: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.viewmodel = viewModel

        binding.localeCardView.visibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) View.VISIBLE
            else View.GONE

        setOnclick()
        setObserver()
        return binding.root
    }

    private fun setOnclick() {
        binding.modeLayout.onClick {
            val modes = arrayOf(
                getString(R.string.system_mode),
                getString(R.string.light_mode),
                getString(R.string.dark_mode),
            )

            AlertDialog.Builder(context)
                .setTitle(getString(R.string.mode))
                .setItems(modes) { _, which ->
                    when(which) {
                        0 -> viewModel.setMode(Const.MODE.SYSTEM_MODE)
                        1 -> viewModel.setMode(Const.MODE.LIGHT_MODE)
                        2 -> viewModel.setMode(Const.MODE.DARK_MODE)
                    }
                }
                .show()
        }

        binding.localeLayout.onClick {
            val locales = arrayOf(
                getString(R.string.ko),
                getString(R.string.en),
            )

            AlertDialog.Builder(context)
                .setTitle(getString(R.string.locale))
                .setItems(locales) { _, which ->
                    when(which) {
                        0 -> viewModel.setLocale(Const.LOCALE.KO)
                        1 -> viewModel.setLocale(Const.LOCALE.EN)
                    }
                }
                .show()
        }

        binding.readLayout.onClick {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.read))
                .setMessage(getString(R.string.delete_msg))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.removeRead()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ ->

                }
                .show()
        }
    }

    private fun setObserver() {
        viewModel.modeLiveData.observe(viewLifecycleOwner) {
            when(it) {
                Const.MODE.SYSTEM_MODE -> binding.modeTvSub.text = getString(R.string.system_mode)
                Const.MODE.LIGHT_MODE -> binding.modeTvSub.text = getString(R.string.light_mode)
                Const.MODE.DARK_MODE -> binding.modeTvSub.text = getString(R.string.dark_mode)
            }
        }

        viewModel.localeLiveData.observe(viewLifecycleOwner) {
            when(it) {
                Const.LOCALE.KO -> binding.localeTvSub.text = getString(R.string.ko)
                Const.LOCALE.EN -> binding.localeTvSub.text = getString(R.string.en)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.menu_3).toString())
        }
    }
}