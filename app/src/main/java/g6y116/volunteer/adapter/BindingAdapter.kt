package g6y116.volunteer.adapter

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import g6y116.volunteer.Const
import g6y116.volunteer.R

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("state")
    fun setStateText(view: TextView, state: String?) {
        when(state) {
            Const.STATE.TODO -> {
                view.text = view.context.getString(R.string.chip_state_todo)
                view.setTextColor(ContextCompat.getColor(view.context, R.color.light_text))
            }
            Const.STATE.DOING -> {
                view.text = view.context.getString(R.string.chip_state_doing)
                view.setTextColor(ContextCompat.getColor(view.context, R.color.blue_light))
            }
            Const.STATE.DONE -> {
                view.text = view.context.getString(R.string.chip_state_done)
                view.setTextColor(ContextCompat.getColor(view.context, R.color.light_text))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["sDateInfo", "eDateInfo"], requireAll = true)
    fun setDateInfoText(view: TextView, startDate: String?, endDate: String?) {
        if (startDate != null && endDate != null) {
            if (startDate == endDate) {
                view.text = view.context.getString(
                    R.string.date_form_short,
                    startDate.substring(4, 6),
                    startDate.substring(6, 8)
                )
            } else {
                view.text = view.context.getString(
                    R.string.date_form,
                    startDate.substring(4, 6),
                    startDate.substring(6, 8),
                    endDate.substring(4, 6),
                    endDate.substring(6, 8),
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["sDate", "eDate"], requireAll = true)
    fun setDateText(view: TextView, startDate: String?, endDate: String?) {
        if (startDate != null && endDate != null) {
            if (startDate == endDate) {
                view.text = view.context.getString(
                    R.string.date_form_short,
                    startDate.substring(4, 6),
                    startDate.substring(6, 8)
                )
            } else {
                view.text = view.context.getString(
                    R.string.date_form,
                    startDate.substring(4, 6),
                    startDate.substring(6, 8),
                    endDate.substring(4, 6),
                    endDate.substring(6, 8),
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["sHour", "eHour"], requireAll = true)
    fun setHourText(view: TextView, sHour: String?, eHour: String?) {
        if (sHour != null && eHour != null)
            view.text = view.context.getString(R.string.time_form, sHour, eHour)
    }

    @JvmStatic
    @BindingAdapter(value = ["isAdult", "isYoung"], requireAll = true)
    fun setPossibleText(view: TextView, isAdult: Boolean?, isYoung: Boolean?) {
        if (isAdult != null && isYoung != null) {
            view.text =
                if (isAdult && isYoung) view.context.getString(R.string.chip_type_all)
                else if (isAdult && !isYoung) view.context.getString(R.string.chip_type_adult)
                else if (!isAdult && isYoung) view.context.getString(R.string.chip_type_young)
                else ""
        }
    }
}