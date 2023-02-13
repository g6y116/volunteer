package g6y116.volunteer

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.text.SimpleDateFormat
import java.util.Date

fun View.onClick(onSingleClick: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener { onSingleClick(it) })
}

fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun log(message: String) {
    val formatStrategy = PrettyFormatStrategy
        .newBuilder()
        .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
        .methodCount(2) // (Optional) How many method line to show. Default 2
        .methodOffset(0) // (Optional) Hides internal method calls up to offset. Default 5
        .tag("로그") // (Optional) Global tag for every log. Default PRETTY_LOGGER
        .build()
    Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    Logger.d(message)
}

@SuppressLint("SimpleDateFormat")
fun longTo8String(timeMillis: Long): String {
    return SimpleDateFormat("yyyyMMdd").format(Date(timeMillis))
}

class OnSingleClickListener(
    private var interval: Int = 250,
    private var onSingleClick: (v: View) -> Unit
) : View.OnClickListener {
    private var lastClickTime: Long = 0
    override fun onClick(v: View) {
        val elapsedRealtime = SystemClock.elapsedRealtime()
        if ((elapsedRealtime - lastClickTime) < interval) return
        lastClickTime = elapsedRealtime
        onSingleClick(v)
    }
}

fun main() {
    println("")
}