package g6y116.volunteer

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Util {
    @SuppressLint("SimpleDateFormat")
    fun getDate(date: Date = Date()): String = SimpleDateFormat("yyyyMMdd").format(date)
}

fun main() {

}