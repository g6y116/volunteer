package g6y116.volunteer

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setLogger()
    }

    private fun setLogger() {
        val formatStrategy = PrettyFormatStrategy
            .newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(2) // (Optional) How many method line to show. Default 2
            .methodOffset(0) // (Optional) Hides internal method calls up to offset. Default 5
            .tag("로그") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}

