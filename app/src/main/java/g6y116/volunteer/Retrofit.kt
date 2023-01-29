package g6y116.volunteer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(Const.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API : Api by lazy { retrofit.create(Api::class.java) }
}