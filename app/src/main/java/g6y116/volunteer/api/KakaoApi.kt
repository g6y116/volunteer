package g6y116.volunteer.api

import g6y116.volunteer.data.KakaoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApi {
    @GET("v2/local/search/address.json")
    suspend fun getAddress(
        @Query("query") query: String,
    ) : Response<KakaoResponse>
}