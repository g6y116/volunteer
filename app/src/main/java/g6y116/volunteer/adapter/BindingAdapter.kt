package g6y116.volunteer.adapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import g6y116.volunteer.Const
import g6y116.volunteer.R
import org.apache.commons.text.StringEscapeUtils

object BindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["sDateInfo", "eDateInfo"], requireAll = true)
    fun setDateInfoText(view: TextView, startDate: String, endDate: String) {
        view.text = startDate.substring(4, 6) + "/" + startDate.substring(6, 8) + " ~ " + endDate.substring(4, 6) + "/" + endDate.substring(6, 8)
        view.setTextColor(ContextCompat.getColor(view.context, R.color.sky_blue))
    }

    @JvmStatic
    @BindingAdapter(value = ["sDate", "eDate"], requireAll = true)
    fun setDateText(view: TextView, startDate: String?, endDate: String?) {
        if (startDate != null && endDate != null) {
            if (startDate == endDate)
                view.text = "${startDate.substring(4, 6).toInt()}월 ${startDate.substring(6, 8).toInt()}일"
            else
                view.text = "${startDate.substring(4, 6).toInt()}월 ${startDate.substring(6, 8).toInt()}일 ~ ${endDate.substring(4, 6).toInt()}월 ${endDate.substring(6, 8).toInt()}일"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["sHour", "eHour"], requireAll = true)
    fun setHourText(view: TextView, sHour: String, eHour: String) {
        view.text = "${sHour.toInt()}시 ~ ${eHour.toInt()}시"
    }

    @JvmStatic
    @BindingAdapter("state")
    fun setStateText(view: TextView, state: Int) {
         when(state) {
            Const.TODO_NUM -> {
                view.text = Const.TODO_TEXT
                view.setTextColor(ContextCompat.getColor(view.context, R.color.gray_1))
            }
            Const.DOING_NUM -> {
                view.text = Const.DOING_TEXT
                view.setTextColor(ContextCompat.getColor(view.context, R.color.sky_blue))
            }
            Const.DONE_NUM -> {
                view.text = Const.DONE_TEXT
                view.setTextColor(ContextCompat.getColor(view.context, R.color.gray_0))
            }
        }

    }

    @JvmStatic
    @BindingAdapter(value = ["adultPossible", "youngPossible"], requireAll = true)
    fun setPossibleText(view: TextView, adultPossible: String?, youngPossible: String?) {
        if (adultPossible == null || youngPossible == null) return

        view.text = when {
            adultPossible == Const.TRUE && youngPossible == Const.TRUE -> "성인/청소년 가능"
            adultPossible == Const.TRUE && youngPossible == Const.FALSE -> "성인 가능"
            adultPossible == Const.FALSE && youngPossible == Const.TRUE -> "청소년 가능"
            else -> ""
        }
    }

//    @JvmStatic
//    @BindingAdapter("bookInfoMark")
//    fun setBookMarkInfoImage(view: ImageView, type: Int) {
//        if (type == Const.MARK) {
//            view.setImageResource(R.drawable.ic_bookmark_24)
//            view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
//            view.visibility = View.VISIBLE
//        }
//        else {
//            view.setImageResource(R.drawable.ic_bookmark_border_24)
//            view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
//            view.visibility = View.GONE
//        }
//    }
//
//    @JvmStatic
//    @BindingAdapter("bookMark")
//    fun setBookMarkImage(view: ImageView, type: Int) {
//        if (type == Const.MARK) view.setImageResource(R.drawable.ic_bookmark_24)
//        else view.setImageResource(R.drawable.ic_bookmark_border_24)
//        view.setColorFilter(ContextCompat.getColor(view.context, R.color.gray_1))
//    }
}