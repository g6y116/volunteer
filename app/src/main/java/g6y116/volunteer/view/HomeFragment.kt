package g6y116.volunteer.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import g6y116.volunteer.*
import g6y116.volunteer.adapter.HomeAdapter
import g6y116.volunteer.adapter.ViewHolderBindListener
import g6y116.volunteer.bottomsheet.RoundedBottomListSheet
import g6y116.volunteer.data.Info
import g6y116.volunteer.data.SearchOption
import g6y116.volunteer.databinding.FragmentHomeBinding
import g6y116.volunteer.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Math.min
import java.util.*
import kotlin.math.max


class HomeFragment : Fragment(), ViewHolderBindListener {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by activityViewModels()
    private val adapter: HomeAdapter = HomeAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.rv.itemAnimator = null

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.sidoChip.onClick {
            viewModel.searchOptionLiveData.value?.let { recentSearch ->
                RoundedBottomListSheet(getString(R.string.chip_area), Code.전국.map { it.name }) { siDoName ->
                    val sidoCode = Code.getSiDo(siDoName)?.code
                    viewModel.setSearchOption(recentSearch.copy(siDoCode = sidoCode, gooGunCode = null))
                }.show((activity as MainActivity).supportFragmentManager, tag)
            }
        }

        binding.googunChip.onClick {
            viewModel.searchOptionLiveData.value?.let { recentSearch ->
                if (recentSearch.siDoCode != null) {
                    RoundedBottomListSheet(getString(R.string.chip_area), Code.getGooGunList(recentSearch.siDoCode).map { it.name }) { gooGunName ->
                        val gooGunCode = Code.getGooGun(recentSearch.siDoCode, gooGunName)?.code
                        viewModel.setSearchOption(recentSearch.copy(gooGunCode = gooGunCode))
                    }.show((activity as MainActivity).supportFragmentManager, tag)
                }
            }
        }

        binding.startDateChip.onClick {
            callDatePicker(
                getString(R.string.chip_start_date),
                positive = { time ->
                    viewModel.searchOptionLiveData.value?.let { recentSearch ->
                        viewModel.setSearchOption(
                            if (recentSearch.eDate.isNullOrEmpty()) recentSearch.copy(sDate = longTo8String(time), eDate = longTo8String(time))
                            else {
                                val sDate = min(longTo8String(time).toInt(), recentSearch.eDate.toInt()).toString()
                                val eDate = max(longTo8String(time).toInt(), recentSearch.eDate.toInt()).toString()
                                recentSearch.copy(sDate = sDate, eDate = eDate)
                            }
                        )
                    }
                },
                negative = {
                    viewModel.searchOptionLiveData.value?.let { recentSearch ->
                        viewModel.setSearchOption(recentSearch.copy(sDate = null, eDate = null))
                    }
                })
        }

        binding.endDateChip.onClick {
            callDatePicker(
                getString(R.string.chip_end_date),
                positive = { time ->
                    viewModel.searchOptionLiveData.value?.let { recentSearch ->
                        viewModel.setSearchOption(
                            if (recentSearch.sDate.isNullOrEmpty()) recentSearch.copy(sDate = longTo8String(time), eDate = longTo8String(time))
                            else {
                                val sDate = min(longTo8String(time).toInt(), recentSearch.sDate.toInt()).toString()
                                val eDate = max(longTo8String(time).toInt(), recentSearch.sDate.toInt()).toString()
                                recentSearch.copy(sDate = sDate, eDate = eDate)
                            }
                        )
                    }
                },
                negative = {
                    viewModel.searchOptionLiveData.value?.let { recentSearch ->
                        viewModel.setSearchOption(recentSearch.copy(sDate = null, eDate = null))
                    }
                })
        }

        binding.resetChip.onClick {
            viewModel.searchOptionLiveData.value?.let {
                viewModel.setSearchOption(SearchOption.reset())
            }
        }

        binding.typeChip.onClick {
            RoundedBottomListSheet(
                getString(R.string.chip_type),
                listOf(getString(R.string.chip_type_all), getString(R.string.chip_type_adult), getString(R.string.chip_type_young)))
            { item ->
                viewModel.searchOptionLiveData.value?.let { searchOption ->
                    when(item) {
                        getString(R.string.chip_type_all) ->
                            viewModel.setSearchOption(searchOption.copy(isAdult = Const.TYPE.NO_MATTER, isYoung = Const.TYPE.NO_MATTER))
                        getString(R.string.chip_type_adult) ->
                            viewModel.setSearchOption(searchOption.copy(isAdult = Const.TYPE.TRUE, isYoung = Const.TYPE.NO_MATTER))
                        getString(R.string.chip_type_young) ->
                            viewModel.setSearchOption(searchOption.copy(isAdult = Const.TYPE.NO_MATTER, isYoung = Const.TYPE.TRUE))
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.stateChip.onClick {
            RoundedBottomListSheet(
                getString(R.string.chip_state),
                listOf(getString(R.string.chip_state_all), getString(R.string.chip_state_doing), getString(R.string.chip_state_done))
            ) { item ->
                viewModel.searchOptionLiveData.value?.let { searchOption ->
                    when(item) {
                        getString(R.string.chip_state_all) -> viewModel.setSearchOption(searchOption.copy(state = Const.STATE.ALL))
                        getString(R.string.chip_state_doing) -> viewModel.setSearchOption(searchOption.copy(state = Const.STATE.DOING))
                        getString(R.string.chip_state_done) -> viewModel.setSearchOption(searchOption.copy(state = Const.STATE.DONE))
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.infoFlowList.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewModel.bookmarkLiveList.observe(viewLifecycleOwner) {
            if (viewModel.contextMenuClickPosition.value == null) adapter.notifyDataSetChanged()
            else {
                adapter.notifyItemChanged(viewModel.contextMenuClickPosition.value!!)
                viewModel.contextMenuClickPosition.value = null
            }
        }

        viewModel.visitLiveList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.searchOptionLiveData.observe(viewLifecycleOwner) {

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

            Handler(Looper.getMainLooper()).postDelayed({
                binding.rv.scrollToPosition(0)
            }, 100)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.toolbar_home).toString())
        }
    }

    override fun onViewHolderBind(holder: ViewHolder, item: Any) {
        val item = item as Info
        val isBookmark = item.isBookmark(viewModel.bookmarkLiveList.value)
        val isVisit = item.isVisit(viewModel.visitLiveList.value)
        val visitOption = viewModel.visitOptionLiveData.value == Const.VISIT.VISIBLE

        holder.itemView.findViewById<ImageView>(R.id.bookmarkIv).visibility = if (isBookmark) View.VISIBLE else View.GONE
        holder.itemView.findViewById<View>(R.id.labelV).visibility = if (isVisit && visitOption) View.VISIBLE else View.GONE
        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            if (isBookmark) contextMenu.add(0, item.pID.toInt(), 0, getString(R.string.delete_bookmark))
            else contextMenu.add(0, item.pID.toInt(), 0, getString(R.string.add_bookmark))
            viewModel.contextMenuClickPosition.value = holder.layoutPosition
        }

        holder.itemView.onClick {
            lifecycleScope.launch {
                startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra("pID", item.pID)
                    putExtra("url", item.url)
                    putExtra("from", Const.FROM.HOME)
                })
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (getString(R.string.add_bookmark) == item.toString() || getString(R.string.delete_bookmark) == item.toString()) {
            viewModel.clickContextMenu(item.itemId.toString(), adapter.snapshot().items)
        }

        return true
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