package g6y116.volunteer

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

@Entity(tableName = "volunteer_info")
data class VolunteerInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("progrmRegistNo") val pID: Int,
    @SerializedName("progrmSj") val title: String,
    @SerializedName("nanmmbyNm") val host: String = "",
    @SerializedName("progrmBgnde") val sDate: Int,
    @SerializedName("progrmEndde") val eDate: Int,
    @SerializedName("progrmSttusSe") val state: Int,
    @SerializedName("sidoCd") val siDoCode: String,
    @SerializedName("gugunCd") val gooGunCode: String,
    @SerializedName("adultPosblAt") val isAdultPossible: String = Const.TRUE, // 성인 가능 여부
    @SerializedName("yngbgsPosblAt") val isYoungPossible: String = Const.FALSE, // 청소년 가능 여부
    @SerializedName("srvcClCode") val field: String, // 분야
    @SerializedName("actPlace") val place: String,
    @SerializedName("actBeginTm") val sHour: Int,
    @SerializedName("actEndTm") val eHour: Int,
    @SerializedName("noticeBgnde") val nsDate: Int, // 모집 시작일
    @SerializedName("noticeEndde") val neDate: Int, // 모집 종료일
    @SerializedName("url") val url: String?,
    var isBookMark: Boolean = false
)

@Entity(tableName = "volunteer")
data class Volunteer(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("progrmRegistNo") val pID: Int,
    @SerializedName("progrmSj") val title: String,
    @SerializedName("progrmSttusSe") val state: Int,
    @SerializedName("progrmBgnde") val sDate: Int,
    @SerializedName("progrmEndde") val eDate: Int,
    @SerializedName("actBeginTm") val sHour: Int,
    @SerializedName("actEndTm") val eHour: Int,
    @SerializedName("noticeBgnde") val nsDate: Int, // 모집 시작일
    @SerializedName("noticeEndde") val neDate: Int, // 모집 종료일
    @SerializedName("rcritNmpr") val num: Int,
    @SerializedName("srvcClCode") val field: String, // 분야
    @SerializedName("adultPosblAt") val isAdultPossible: Boolean, // 성인 가능 여부
    @SerializedName("yngbgsPosblAt") val isYoungPossible: Boolean, // 청소년 가능 여부
    @SerializedName("grpPosblAt") val isGroupPossible: Boolean, // 단체 가능 여부
    @SerializedName("nanmmbyNm") val host: String,
    @SerializedName("actPlace") val place: String,
    @SerializedName("nanmmbyNmAdmn") val manager: String,
    @SerializedName("telno") val tel: String,
    @SerializedName("progrmCn") val contents: String,
    @SerializedName("sidoCd") val siDoCode: String,
    @SerializedName("gugunCd") val gooGunCode: String,
    var isBookMark: Boolean = false
)

@Dao
interface VolunteerInfoDao {

}

@Dao
interface VolunteerDao {

}

interface Api {
    @GET("getVltrSearchWordList")
    suspend fun getVolunteerList(
        @Query("progrmBgnde") sDate: Int,
        @Query("progrmEndde") eDate: Int,
        @Query("adultPosblAt") isAdultPossible: String,
        @Query("yngbgsPosblAt") isYoungPossible: String,
        @Query("pageNo") pageNum: Int,
        @Query("keyword") keyWord: String, // 옵션
        @Query("schSido") siDoCode: String,
        @Query("schSign1") gooGunCode: String,
    ) : Response<List<VolunteerInfo>>

    @GET("getVltrPartcptnItem")
    suspend fun getVolunteerDetail(
        @Query("progrmRegistNo") pID: Int,
    ) : Response<Volunteer>
}

interface Repository {
    fun getVolunteerList(
        sDate: Int,
        eDate: Int,
        siDoCode: String = "",
        gooGunCode: String = "",
        pageNum: Int,
        keyWord: String? = null,
        isAdultPossible: String = "Y",
        isYoungPossible: String = "Y",
    ): List<VolunteerInfo>
    fun getBookMarkList(): List<VolunteerInfo>
    fun addBookMark(pID: Int)
    fun deleteBookMark(pID: Int)
    fun getVolunteerDetail(pID: Int): Volunteer
}

class RepositoryImpl : Repository {
    override fun getVolunteerList(
        sDate: Int,
        eDate: Int,
        siDoCode: String,
        gooGunCode: String,
        pageNum: Int,
        keyWord: String?,
        isAdultPossible: String,
        isYoungPossible: String
    ): List<VolunteerInfo> {
        TODO("Not yet implemented")
    }

    override fun getBookMarkList(): List<VolunteerInfo> {
        TODO("Not yet implemented")
    }

    override fun addBookMark(pID: Int) {
        TODO("Not yet implemented")
    }

    override fun deleteBookMark(pID: Int) {
        TODO("Not yet implemented")
    }

    override fun getVolunteerDetail(pID: Int): Volunteer {
        TODO("Not yet implemented")
    }
}