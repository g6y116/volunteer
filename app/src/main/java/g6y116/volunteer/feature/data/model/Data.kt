package g6y116.volunteer.feature.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

data class MainUiState(
    val pID: String,
    val title: String,
    val host: String,
    val sDate: String,
    val eDate: String,
    val state: String,
    val url: String,
    var isBookmark: Boolean = true,
    var isVisit: Boolean = true,
): java.io.Serializable

data class DetailUiState(
    val pID: String,
    val title: String,
    val area: String,
    val host: String,
    val sDate: String,
    val eDate: String,
    val state: String,
    val isAdult: Boolean, // 성인 가능 여부
    val isYoung: Boolean, // 청소년 가능 여부
    val field: String, // 분야
    val place: String,
    val sHour: Int,
    val eHour: Int,
    val nsDate: String, // 모집 시작일
    val neDate: String, // 모집 종료일
    val person: Int,
    val manager: String,
    val tel: String,
    val email: String,
    val contents: String,
    val address: String,
    var isBookmark: Boolean,
    val url: String,
): java.io.Serializable

@Entity(tableName = "info")
data class Info(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "p_id") val pID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "s_date") val sDate: String,
    @ColumnInfo(name = "e_date") val eDate: String,
    @ColumnInfo(name = "state") val state: String,
    @ColumnInfo(name = "url") val url: String,
) {
    fun isBookmark(items: List<Info>?): Boolean =
        if (items.isNullOrEmpty()) false else items.any { it.pID == this.pID }

    fun isVisit(items: List<Visit>?): Boolean =
        if (items.isNullOrEmpty()) false else items.any { it.pID == this.pID }
}

data class Volunteer(
    val pID: String,
    val title: String,
    val area: String,
    val host: String,
    val sDate: String,
    val eDate: String,
    val state: String,
    val siDoCode: String,
    val gooGunCode: String,
    val isAdult: Boolean, // 성인 가능 여부
    val isYoung: Boolean, // 청소년 가능 여부
    val field: String, // 분야
    val place: String,
    val sHour: Int,
    val eHour: Int,
    val nsDate: String, // 모집 시작일
    val neDate: String, // 모집 종료일
    val person: Int,
    val manager: String,
    val tel: String,
    val email: String,
    val contents: String,
    val address: String,
)

@Entity(tableName = "visit")
data class Visit(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "p_id") val pID: String,
)

data class Coordinate(
    val x: String, // 경도
    val y: String, // 위도
)

data class SearchOption(
    val siDoCode: String?,
    val gooGunCode: String?,
    val sDate: String?,
    val eDate: String?,
    val isAdult: String?,
    val isYoung: String?,
    val keyWord: String?,
    val state: String?,
)

data class AppOption(
    val theme: String,
    val language: String,
    val visit: String,
)