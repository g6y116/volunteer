package g6y116.volunteer.api

import g6y116.volunteer.Const
import g6y116.volunteer.data.DetailResponse
import g6y116.volunteer.data.HomeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VolunteerApi {
    @GET("getVltrSearchWordList")
    suspend fun getVolunteerList(
        @Query("pageNo") pageNum: Int,
        @Query("schSido") siDoCode: String,
        @Query("schSign1") gooGunCode: String,
        @Query("progrmBgnde") sDate: String,
        @Query("progrmEndde") eDate: String,
        @Query("keyword") keyWord: String,
        @Query("adultPosblAt") isAdultPossible: String,
        @Query("yngbgsPosblAt") isYoungPossible: String,
        @Query("serviceKey") serviceKey: String = Const.SERVICE_KEY,
        @Query("numOfRows") numOfRows: Int = Const.LOAD_SIZE,
    ) : Response<HomeResponse>

    @GET("getVltrPartcptnItem")
    suspend fun getVolunteerDetail(
        @Query("progrmRegistNo") pID: String,
        @Query("serviceKey") serviceKey: String = Const.SERVICE_KEY,
    ) : Response<DetailResponse>
}