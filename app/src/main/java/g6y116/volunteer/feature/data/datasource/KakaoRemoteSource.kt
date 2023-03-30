package g6y116.volunteer.feature.data.datasource

import g6y116.volunteer.feature.data.model.KakaoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoRemoteSource {

    @GET("v2/local/search/address.json")
    suspend fun loadAddress(
        @Query("query") query: String,
    ) : Response<KakaoResponse>
}