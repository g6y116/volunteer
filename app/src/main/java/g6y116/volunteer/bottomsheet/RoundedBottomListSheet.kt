package g6y116.volunteer.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import g6y116.volunteer.R
import g6y116.volunteer.databinding.BottomSheetListBinding
import g6y116.volunteer.databinding.ItemBottomSheetBinding
import g6y116.volunteer.onClick

class RoundedBottomListSheet<T: Any>(
    private val label: String,
    private val items: List<T>,
    private val listener: (item: T) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.bottom_sheet)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = BottomSheetListBinding.inflate(layoutInflater)
        binding.labelTv.text = label
        binding.rv.adapter = Adapter()
        binding.rv.layoutManager = GridLayoutManager(context, 3)

        binding.cancelIv.onClick {
            dismiss()
        }

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        return binding.root
    }

    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

        inner class ViewHolder(
            private val binding: ItemBottomSheetBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(item: T) {
                binding.itemTv.text = item.toString()
                binding.root.onClick {
                    listener.invoke(item)
                    dismiss()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
        override fun getItemCount(): Int = items.size
    }
}