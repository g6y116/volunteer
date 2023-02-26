package g6y116.volunteer.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import g6y116.volunteer.data.VolunteerInfo
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.recyclerView.itemAnimator = null

        setOnclick()
        setObserver()
    }

    private fun setOnclick() {
        binding.sidoChip.onClick {
            viewModel.recentSearchLiveData.value?.let { recentSearch ->
                RoundedBottomListSheet(getString(R.string.choice_area), Code.전국.map { it.name }) { siDoName ->
                    viewModel.setRecentSearch(recentSearch.copy(siDoCode = Code.getSiDo(siDoName)?.code, gooGunCode = null))
                }.show((activity as MainActivity).supportFragmentManager, tag)
            }
        }

        binding.googunChip.onClick {
            viewModel.recentSearchLiveData.value?.let { recentSearch ->
                RoundedBottomListSheet(getString(R.string.choice_area), Code.getGooGunList(recentSearch.siDoCode).map { it.name }) { gooGunName ->
                    val gooGunCode = if (recentSearch.siDoCode != null) Code.getGooGun(recentSearch.siDoCode, gooGunName)?.code else null
                    viewModel.setRecentSearch(recentSearch.copy(gooGunCode = gooGunCode))
                }.show((activity as MainActivity).supportFragmentManager, tag)
            }
        }

        binding.startDateChip.onClick {
            callDatePicker(
                getString(R.string.start_date),
                positive = { time ->
                    viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(
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
                    viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(recentSearch.copy(sDate = null, eDate = null))
                    }
                })
        }

        binding.endDateChip.onClick {
            callDatePicker(
                getString(R.string.end_date),
                positive = { time ->
                    viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(
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
                    viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(recentSearch.copy(sDate = null, eDate = null))
                    }
                })
        }

        binding.typeChip.onClick {
            RoundedBottomListSheet(getString(R.string.choice_type), listOf(Const.TYPE.BOTH, Const.TYPE.ADULT, Const.TYPE.YOUNG)) { item ->
                when(item) {
                    Const.TYPE.BOTH -> viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(recentSearch.copy(isAdultPossible = Const.TYPE.NO_MATTER, isYoungPossible = Const.TYPE.NO_MATTER))
                    }
                    Const.TYPE.ADULT -> viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(recentSearch.copy(isAdultPossible = Const.TYPE.TRUE, isYoungPossible = Const.TYPE.NO_MATTER))
                    }
                    Const.TYPE.YOUNG -> viewModel.recentSearchLiveData.value?.let { recentSearch ->
                        viewModel.setRecentSearch(recentSearch.copy(isAdultPossible = Const.TYPE.NO_MATTER, isYoungPossible = Const.TYPE.TRUE))
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.resetChip.onClick {
            viewModel.recentSearchLiveData.value?.let {
                viewModel.removeRecentSearch()
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

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeList.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

        viewModel.bookMarkList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.readList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.recentSearchLiveData.observe(viewLifecycleOwner) {

            var sidoString = Code.getSiDo(it.siDoCode)?.name ?: getString(R.string.sido)
                if (sidoString == "전국") sidoString = getString(R.string.sido)
            val googunString =
                if (it.siDoCode != null && it.gooGunCode != null)
                    Code.getGooGun(it.siDoCode, it.gooGunCode)?.name ?: getString(R.string.googun)
                else
                    getString(R.string.googun)

            binding.sidoChip.text = sidoString
            binding.googunChip.text = googunString

            if (sidoString == getString(R.string.sido) || sidoString == "제주도" || sidoString == "세종")
                binding.googunChip.visibility = View.GONE
            else
                binding.googunChip.visibility = View.VISIBLE

            binding.startDateChip.text =
                if (!it.sDate.isNullOrBlank())
                    "${it.sDate.substring(4, 6).toInt()}월 ${it.sDate.substring(6, 8).toInt()}일"
                else getString(R.string.start_date)

            binding.endDateChip.text =
                if (!it.eDate.isNullOrBlank())
                    "${it.eDate.substring(4, 6).toInt()}월 ${it.eDate.substring(6, 8).toInt()}일"
                else getString(R.string.end_date)

            binding.typeChip.text = when {
                it.isAdultPossible == Const.TYPE.NO_MATTER && it.isYoungPossible == Const.TYPE.NO_MATTER -> getString(R.string.adult_young)
                it.isAdultPossible == Const.TYPE.TRUE && it.isYoungPossible == Const.TYPE.NO_MATTER -> getString(R.string.adult)
                it.isAdultPossible == Const.TYPE.NO_MATTER && it.isYoungPossible == Const.TYPE.TRUE -> getString(R.string.young)
                else -> getString(R.string.type)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { mainActivity ->
            (mainActivity as MainActivity).setToolbarTitle(getText(R.string.menu_1).toString())
        }
    }

    override fun onViewHolderBind(holder: ViewHolder, item: Any) {
        val item = item as VolunteerInfo

        holder.itemView.findViewById<ImageView>(R.id.ivBookMark).visibility =
            if (item.isBookMark(viewModel.bookMarkList.value)) View.VISIBLE else View.GONE

        holder.itemView.findViewById<ImageView>(R.id.ivRead).visibility =
            if (item.isRead(viewModel.readList.value)) View.VISIBLE else View.GONE

        holder.itemView.onClick {
            lifecycleScope.launch {
                startActivity(Intent(context, DetailActivity::class.java).apply {
                    putExtra("pID", item.pID)
                    putExtra("url", item.url)
                    putExtra("from", Const.HOME)
                })
            }
        }
    }
}