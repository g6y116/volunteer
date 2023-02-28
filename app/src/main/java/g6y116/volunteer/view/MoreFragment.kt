package g6y116.volunteer.view

import android.app.AlertDialog
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.localeCardView.visibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) View.VISIBLE
            else View.GONE

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.modeLayout.onClick {
            val menus = arrayOf(getString(R.string.theme_system), getString(R.string.theme_light), getString(R.string.theme_dark))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.theme))
                .setItems(menus) { _, which ->
                    when(which) {
                        0 -> viewModel.setThemeOption(Const.THEME.SYSTEM)
                        1 -> viewModel.setThemeOption(Const.THEME.LIGHT)
                        2 -> viewModel.setThemeOption(Const.THEME.DARK)
                    }
                }
                .show()
        }

        binding.localeLayout.onClick {
            val menus = arrayOf(getString(R.string.language_ko), getString(R.string.language_en))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.language))
                .setItems(menus) { _, which ->
                    when(which) {
                        0 -> viewModel.setLanguageOption(Const.LANGUAGE.KOREAN)
                        1 -> viewModel.setLanguageOption(Const.LANGUAGE.ENGLISH)
                    }
                }
                .show()
        }

        binding.readLayout.onClick {
            val menus = arrayOf(getString(R.string.visit_visible), getString(R.string.visit_invisible), getString(R.string.visit_delete))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.visit))
                .setItems(menus) { _, which ->
                    when(which) {
                        0 -> viewModel.setVisitOption(Const.VISIT.VISIBLE)
                        1 -> viewModel.setVisitOption(Const.VISIT.INVISIBLE)
                        2 -> {
                            AlertDialog.Builder(context)
                                .setTitle(getString(R.string.visit))
                                .setMessage(getString(R.string.msg_visit_delete))
                                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                                    viewModel.deleteVisitHistory()
                                }
                                .setNegativeButton(getString(R.string.no)) { _, _ -> }
                                .show()
                        }
                    }
                }
                .show()
        }
    }

    private fun setObserver() {
        viewModel.themeOptionLiveData.observe(viewLifecycleOwner) {
            when(it) {
                Const.THEME.SYSTEM -> binding.modeTvSub.text = getString(R.string.theme_system)
                Const.THEME.LIGHT -> binding.modeTvSub.text = getString(R.string.theme_light)
                Const.THEME.DARK -> binding.modeTvSub.text = getString(R.string.theme_dark)
            }
        }

        viewModel.languageOptionLiveData.observe(viewLifecycleOwner) {
            when(it) {
                Const.LANGUAGE.KOREAN -> binding.localeTvSub.text = getString(R.string.language_ko)
                Const.LANGUAGE.ENGLISH -> binding.localeTvSub.text = getString(R.string.language_en)
            }
        }

        viewModel.visitOptionLiveData.observe(viewLifecycleOwner) {
            when(it) {
                Const.VISIT.VISIBLE -> binding.readTvSub.text = getString(R.string.visit_visible)
                Const.VISIT.INVISIBLE -> binding.readTvSub.text = getString(R.string.visit_invisible)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.toolbar_more).toString())
        }
    }
}