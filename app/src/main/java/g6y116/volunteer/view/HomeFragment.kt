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
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
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

        lifecycleScope.launch {
            viewModel.homeList.collectLatest {
                adapter.submitData(it)

                if (adapter.itemCount == 0) {
                    binding.noResult.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                } else {
                    binding.rv.visibility = View.VISIBLE
                    binding.noResult.visibility = View.GONE
                }
            }
        }

        viewModel.bookMarkList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        binding.chip1.onClick {
            RoundedBottomListSheet("지역 선택", Code.전국.map { it.name }) { item ->
                log("지역 선택 : ${item}")
            }.show((activity as MainActivity).supportFragmentManager, tag)
        }

        binding.chip2.onClick {
            RoundedBottomListSheet("지역 선택", Code.서울.map { it.name }) { item ->
                log("지역 선택 : ${item}")
            }.show((activity as MainActivity).supportFragmentManager, tag)
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
            }

            datePicker.show((activity as MainActivity).supportFragmentManager, "")
        }

        binding.chip4.onClick {
            RoundedBottomListSheet("유형 선택", listOf(Const.BOTH, Const.ADULT, Const.YOUNG)) { item ->
                log("유형 선택 : ${item}")
            }.show((activity as MainActivity).supportFragmentManager, tag)
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