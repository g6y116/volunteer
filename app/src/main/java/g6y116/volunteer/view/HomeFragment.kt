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
import java.util.*


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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.homeList.collectLatest {
                    adapter.submitData(it)

//                    if (adapter.itemCount == 0) {
//                        binding.noResult.visibility = View.VISIBLE
//                        binding.rv.visibility = View.GONE
//                    } else {
//                        binding.rv.visibility = View.VISIBLE
//                        binding.noResult.visibility = View.GONE
//                    }
                }
            }
        }

        viewModel.bookMarkList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.recentSearch.observe(viewLifecycleOwner) {
            if (it.siDoCode.isNotBlank()) {
                binding.chip1.text = Code.getSiDoName(it.siDoCode)
            } else {
                binding.chip1.text = "시 · 도"
            }

            if (it.gooGunCode.isNotBlank()) {
                binding.chip2.text = Code.getGooGunName(it.siDoCode, it.gooGunCode)
            } else {
                binding.chip2.text = "구 · 군"
            }

            if (it.sDate.isNotBlank() && it.eDate.isNotBlank()) {
                binding.chip3.text = "${it.sDate.substring(4, 6).toInt()}월 ${it.sDate.substring(6, 8).toInt()}일"
            } else {
                binding.chip3.text = "봉사일"
            }

            binding.chip4.text = when {
                it.isAdultPossible == Const.TRUE && it.isYoungPossible == Const.TRUE -> "성인/청소년"
                it.isAdultPossible == Const.TRUE && it.isYoungPossible == Const.FALSE -> "성인"
                it.isAdultPossible == Const.FALSE && it.isYoungPossible == Const.TRUE -> "청소년"
                else -> "봉사유형"
            }
        }

        binding.chip1.onClick {
            RoundedBottomListSheet("지역 선택", Code.전국.map { it.name }) { item ->
                viewModel.recentSearch.value?.let {
                    viewModel.saveRecentSearch(it.copy(siDoCode = Code.getSiDoCode(item)))
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.chip2.onClick {
            if (viewModel.recentSearch.value != null) {
                RoundedBottomListSheet("지역 선택", Code.getGooGunList(viewModel.recentSearch.value!!.siDoCode).map { it.name }) { item ->
                    viewModel.recentSearch.value?.let {
                        viewModel.saveRecentSearch(it.copy(gooGunCode = Code.getGooGunCode(viewModel.recentSearch.value!!.siDoCode, item)))
                    }
                }.show((activity as MainActivity).supportFragmentManager, tag)
            }
        }

        binding.chip3.onClick {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val constraints = CalendarConstraints.Builder()
                .setStart(Calendar.getInstance().apply { add(Calendar.MONTH, -2) }.time.time)
                .setEnd(Calendar.getInstance().apply { add(Calendar.MONTH, 2) }.time.time)
                .build()
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.date_picker)
                .setTitleText("봉사일")
                .setCalendarConstraints(constraints)
                .setSelection(today)
                .build()

            datePicker.addOnPositiveButtonClickListener {
                log("날짜 선택 : ${longTo8String(it)}")
                viewModel.recentSearch.value?.let { recentSearch ->
                    viewModel.saveRecentSearch(recentSearch.copy(
                        sDate = longTo8String(it),
                        eDate = longTo8String(it),
                    ))
                }
            }

            datePicker.show((activity as MainActivity).supportFragmentManager, "")
        }

        binding.chip4.onClick {
            RoundedBottomListSheet("유형 선택", listOf(Const.BOTH, Const.ADULT, Const.YOUNG)) { item ->
                when(item) {
                    Const.BOTH -> viewModel.recentSearch.value?.let { recentSearch ->
                        viewModel.saveRecentSearch(recentSearch.copy(
                            isYoungPossible = Const.TRUE,
                            isAdultPossible = Const.TRUE,
                        ))
                    }

                    Const.ADULT -> viewModel.recentSearch.value?.let { recentSearch ->
                        viewModel.saveRecentSearch(recentSearch.copy(
                            isYoungPossible = Const.FALSE,
                            isAdultPossible = Const.TRUE,
                        ))
                    }

                    Const.YOUNG -> viewModel.recentSearch.value?.let { recentSearch ->
                        viewModel.saveRecentSearch(recentSearch.copy(
                            isYoungPossible = Const.TRUE,
                            isAdultPossible = Const.FALSE,
                        ))
                    }
                }
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.chip5.onClick {
            viewModel.recentSearch.value?.let {
                viewModel.resetRecentSearch()
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