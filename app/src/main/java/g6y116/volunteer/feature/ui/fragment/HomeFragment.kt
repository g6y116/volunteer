package g6y116.volunteer.feature.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import g6y116.volunteer.*
import g6y116.volunteer.base.abstracts.BaseFragment
import g6y116.volunteer.base.codes.Code
import g6y116.volunteer.feature.adapter.HomeFragmentAdapter
import g6y116.volunteer.base.consts.Const
import g6y116.volunteer.base.customviews.BottomSheet
import g6y116.volunteer.base.utils.longTo8String
import g6y116.volunteer.base.utils.onClick
import g6y116.volunteer.base.utils.toast
import g6y116.volunteer.databinding.FragmentHomeBinding
import g6y116.volunteer.feature.ui.activity.DetailActivity
import g6y116.volunteer.feature.ui.activity.MainActivity
import g6y116.volunteer.feature.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Math.min
import java.util.*
import kotlin.math.max


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: MainViewModel by activityViewModels()
    private val adapter: HomeFragmentAdapter = HomeFragmentAdapter { item ->
        viewModel.clickVolunteer(
            item,
            { startActivity(Intent(context, DetailActivity::class.java).apply { putExtra("item", it) }) },
            { toast(requireActivity(), getString(R.string.msg_volunteer_deleted)) }
        )
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.toolbar_home).toString())
        }
    }

    override fun initView() {
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.rv.itemAnimator = null
    }

    override fun setOnclick() {
        binding.sidoChip.onClick {
            viewModel.searchOption.value?.let { recentSearch ->
                BottomSheet(getString(R.string.chip_area), Code.전국.map { it.name }) { siDoName ->
                    lifecycleScope.launch {
                        val sidoCode = Code.getSiDo(siDoName)?.code
                        viewModel.setOption(sidoCode, Const.PrefKey.SIDO, null, Const.PrefKey.GOOGUN)
                    }
                }.show((activity as MainActivity).supportFragmentManager, tag)
            }
        }

        binding.googunChip.onClick {
            viewModel.searchOption.value?.let { recentSearch ->
                if (recentSearch.siDoCode != null) {
                    BottomSheet(getString(R.string.chip_area), Code.getGooGunList(recentSearch.siDoCode).map { it.name }) { gooGunName ->
                        lifecycleScope.launch {
                            val gooGunCode = Code.getGooGun(recentSearch.siDoCode, gooGunName)?.code
                            viewModel.setOption(gooGunCode, Const.PrefKey.GOOGUN)
                        }
                    }.show((activity as MainActivity).supportFragmentManager, tag)
                }
            }
        }

        binding.startDateChip.onClick {
            callDatePicker(
                getString(R.string.chip_start_date),
                positive = { time ->
                    viewModel.searchOption.value?.let { recentSearch ->
                        if (recentSearch.eDate.isNullOrEmpty()) {
                            lifecycleScope.launch {
                                viewModel.setOption(
                                    longTo8String(time),
                                    Const.PrefKey.START_DATE,
                                    longTo8String(time),
                                    Const.PrefKey.END_DATE,
                                )
                            }
                        } else {
                            lifecycleScope.launch {
                                val sDate = min(longTo8String(time).toInt(), recentSearch.eDate.toInt()).toString()
                                val eDate = max(longTo8String(time).toInt(), recentSearch.eDate.toInt()).toString()
                                viewModel.setOption(
                                    sDate,
                                    Const.PrefKey.START_DATE,
                                    eDate,
                                    Const.PrefKey.END_DATE,
                                )
                            }
                        }
                    }
                },
                negative = {
                    viewModel.searchOption.value?.let {
                        lifecycleScope.launch {
                            viewModel.setOption(
                                null,
                                Const.PrefKey.START_DATE,
                                null,
                                Const.PrefKey.END_DATE,
                            )
                        }
                    }
                })
        }

        binding.endDateChip.onClick {
            callDatePicker(
                getString(R.string.chip_end_date),
                positive = { time ->
                    viewModel.searchOption.value?.let { recentSearch ->
                        if (recentSearch.sDate.isNullOrEmpty()) {
                            lifecycleScope.launch {
                                viewModel.setOption(
                                    longTo8String(time),
                                    Const.PrefKey.START_DATE,
                                    longTo8String(time),
                                    Const.PrefKey.END_DATE
                                )
                            }
                        } else {
                            lifecycleScope.launch {
                                val sDate = min(longTo8String(time).toInt(), recentSearch.sDate.toInt()).toString()
                                val eDate = max(longTo8String(time).toInt(), recentSearch.sDate.toInt()).toString()
                                viewModel.setOption(
                                    sDate,
                                    Const.PrefKey.START_DATE,
                                    eDate,
                                    Const.PrefKey.END_DATE,
                                )
                            }
                        }
                    }
                },
                negative = {
                    viewModel.searchOption.value?.let {
                        lifecycleScope.launch {
                            viewModel.setOption(
                                null,
                                Const.PrefKey.START_DATE,
                                null,
                                Const.PrefKey.END_DATE
                            )
                        }
                    }
                })
        }

        binding.resetChip.onClick {
            viewModel.searchOption.value?.let {
                lifecycleScope.launch {
                    viewModel.resetOption()
                }
            }
        }

        binding.typeChip.onClick {
            BottomSheet(
                getString(R.string.chip_type),
                listOf(getString(R.string.chip_type_all), getString(R.string.chip_type_adult), getString(R.string.chip_type_young)))
            { item ->
                viewModel.searchOption.value?.let { searchOption ->
                    when(item) {
                        getString(R.string.chip_type_all) -> {
                            lifecycleScope.launch {
                                viewModel.setOption(Const.TYPE.NO_MATTER, Const.PrefKey.ADULT, Const.TYPE.NO_MATTER, Const.PrefKey.YOUNG)
                            }
                        }
                        getString(R.string.chip_type_adult) -> {
                            lifecycleScope.launch {
                                viewModel.setOption(Const.TYPE.TRUE, Const.PrefKey.ADULT, Const.TYPE.NO_MATTER, Const.PrefKey.YOUNG)
                            }
                        }
                        getString(R.string.chip_type_young) -> {
                            lifecycleScope.launch {
                                viewModel.setOption(Const.TYPE.NO_MATTER, Const.PrefKey.ADULT, Const.TYPE.TRUE, Const.PrefKey.YOUNG)
                            }
                        }
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.stateChip.onClick {
            BottomSheet(
                getString(R.string.chip_state),
                listOf(getString(R.string.chip_state_all), getString(R.string.chip_state_doing), getString(R.string.chip_state_done))
            ) { item ->
                viewModel.searchOption.value?.let { searchOption ->
                    when(item) {
                        getString(R.string.chip_state_all) -> lifecycleScope.launch { viewModel.setOption(Const.STATE.ALL, Const.PrefKey.STATE) }
                        getString(R.string.chip_state_doing) -> lifecycleScope.launch { viewModel.setOption(Const.STATE.DOING, Const.PrefKey.STATE) }
                        getString(R.string.chip_state_done) -> lifecycleScope.launch { viewModel.setOption(Const.STATE.DONE, Const.PrefKey.STATE) }
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    override fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeUiState.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewModel.searchOption.observe(viewLifecycleOwner) {

            viewModel.search(it) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.rv.scrollToPosition(0)
                }, 100)
            }

            val sidoString =
                if (!it.siDoCode.isNullOrEmpty()) Code.getSiDo(it.siDoCode)?.name ?: getString(R.string.chip_area_all)
                else  getString(R.string.chip_area_all)

            val googunString =
                if (!it.siDoCode.isNullOrEmpty() && !it.gooGunCode.isNullOrEmpty())
                    Code.getGooGun(it.siDoCode, it.gooGunCode)?.name ?: getString(R.string.all)
                else getString(R.string.all)

            binding.sidoChip.text = sidoString
            binding.googunChip.text = googunString
            binding.googunChip.visibility =
                if (sidoString == getString(R.string.chip_area_all) || sidoString == "제주도" || sidoString == "세종") View.GONE
                else View.VISIBLE

            binding.startDateChip.text =
                if (!it.sDate.isNullOrEmpty()) getString(R.string.date_form_short, it.sDate.substring(4, 6), it.sDate.substring(6, 8))
                else getString(R.string.chip_start_date)

            binding.endDateChip.text =
                if (!it.eDate.isNullOrEmpty()) getString(R.string.date_form_short, it.eDate.substring(4, 6), it.eDate.substring(6, 8))
                else getString(R.string.chip_end_date)

            binding.typeChip.text = when {
                it.isAdult == Const.TYPE.NO_MATTER && it.isYoung == Const.TYPE.NO_MATTER -> getString(R.string.chip_type_all)
                it.isAdult == Const.TYPE.TRUE && it.isYoung == Const.TYPE.NO_MATTER -> getString(R.string.chip_type_adult)
                it.isAdult == Const.TYPE.NO_MATTER && it.isYoung == Const.TYPE.TRUE -> getString(R.string.chip_type_young)
                else -> getString(R.string.type)
            }

            binding.stateChip.text = when (it.state) {
                Const.STATE.ALL -> getString(R.string.chip_state_all)
                Const.STATE.DOING -> getString(R.string.chip_state_doing)
                Const.STATE.DONE -> getString(R.string.chip_state_done)
                else -> getString(R.string.chip_state_all)
            }
        }
    }

    private fun callDatePicker(titleMsg: String, positive: (Long) -> Unit, negative: () -> Unit) {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraints = CalendarConstraints.Builder()
            .setStart(Calendar.getInstance().apply { add(Calendar.DATE, -60) }.time.time)
            .setEnd(Calendar.getInstance().apply { add(Calendar.DATE, 60) }.time.time)
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.date_picker)
            .setTitleText(titleMsg)
            .setCalendarConstraints(constraints)
            .setSelection(today)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            positive(it)
        }

        datePicker.addOnNegativeButtonClickListener {
            negative()
        }

        datePicker.show((activity as MainActivity).supportFragmentManager, "")
    }
}