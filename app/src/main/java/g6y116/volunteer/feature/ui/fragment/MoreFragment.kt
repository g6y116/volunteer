package g6y116.volunteer.feature.ui.fragment

import android.app.AlertDialog
import android.os.Build
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.R
import g6y116.volunteer.base.abstracts.BaseFragment
import g6y116.volunteer.base.utils.onClick
import g6y116.volunteer.databinding.FragmentMoreBinding
import g6y116.volunteer.feature.ui.activity.MainActivity
import g6y116.volunteer.feature.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MoreFragment : BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    override val viewModel: MainViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.toolbar_more).toString())
        }
    }

    override fun initView() {
        binding.viewmodel = viewModel
        binding.languageCv.visibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) View.VISIBLE
            else View.GONE
    }

    override fun setOnclick() {
        binding.themeL.onClick {
            val menus = arrayOf(getString(R.string.theme_system), getString(R.string.theme_light), getString(R.string.theme_dark))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.theme))
                .setItems(menus) { _, which ->
                    lifecycleScope.launch {
                        when(which) {
                            0 -> viewModel.setOption(Const.THEME.SYSTEM, Const.PrefKey.THEME)
                            1 -> viewModel.setOption(Const.THEME.LIGHT, Const.PrefKey.THEME)
                            2 -> viewModel.setOption(Const.THEME.DARK, Const.PrefKey.THEME)
                        }
                    }
                }
                .show()
        }

        binding.languageL.onClick {
            val menus = arrayOf(getString(R.string.language_ko), getString(R.string.language_en))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.language))
                .setItems(menus) { _, which ->
                    lifecycleScope.launch {
                        when(which) {
                            0 -> viewModel.setOption(Const.LANGUAGE.KOREAN, Const.PrefKey.LANGUAGE)
                            1 -> viewModel.setOption(Const.LANGUAGE.ENGLISH, Const.PrefKey.LANGUAGE)
                        }
                    }
                }
                .show()
        }

        binding.visitL.onClick {
            val menus = arrayOf(getString(R.string.visit_visible), getString(R.string.visit_invisible), getString(R.string.visit_delete))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.visit))
                .setItems(menus) { _, which ->
                    lifecycleScope.launch {
                        when (which) {
                            0 -> viewModel.setOption(Const.VISIT.VISIBLE, Const.PrefKey.VISIT)
                            1 -> viewModel.setOption(Const.VISIT.INVISIBLE, Const.PrefKey.VISIT)
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
                }
                .show()
        }
    }

    override fun setObserver() {
        viewModel.appOption.observe(viewLifecycleOwner) {
            when(it.theme) {
                Const.THEME.SYSTEM -> binding.themeSubTv.text = getString(R.string.theme_system)
                Const.THEME.LIGHT -> binding.themeSubTv.text = getString(R.string.theme_light)
                Const.THEME.DARK -> binding.themeSubTv.text = getString(R.string.theme_dark)
            }

            when(it.language) {
                Const.LANGUAGE.KOREAN -> binding.languageSubTv.text = getString(R.string.language_ko)
                Const.LANGUAGE.ENGLISH -> binding.languageSubTv.text = getString(R.string.language_en)
            }

            when(it.visit) {
                Const.VISIT.VISIBLE -> binding.visitSubTv.text = getString(R.string.visit_visible)
                Const.VISIT.INVISIBLE -> binding.visitSubTv.text = getString(R.string.visit_invisible)
            }

            viewModel.applyTheme(it.theme)
            viewModel.applyLanguage(it.language)
        }
    }
}