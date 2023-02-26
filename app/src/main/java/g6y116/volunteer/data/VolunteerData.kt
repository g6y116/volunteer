package g6y116.volunteer.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "volunteer_info")
data class VolunteerInfo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "p_id") val pID: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "host") val host: String,
    @ColumnInfo(name = "s_date") val sDate: String,
    @ColumnInfo(name = "e_date") val eDate: String,
    @ColumnInfo(name = "state") val state: Int,
    @ColumnInfo(name = "url") val url: String,
) {
    fun isBookMark(items: List<VolunteerInfo>?): Boolean =
        if (items.isNullOrEmpty()) false else items.any { it.pID == this.pID }

    fun isRead(items: List<Read>?): Boolean =
        if (items.isNullOrEmpty()) false else items.any { it.pID == this.pID }
}

@Entity(tableName = "volunteer_read")
data class Read(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "p_id") val pID: String,
)

data class Volunteer(
    val pID: String,
    val title: String,
    val area: String,
    val host: String,
    val sDate: String,
    val eDate: String,
    val state: Int,
    val siDoCode: String,
    val gooGunCode: String,
    val isAdultPossible: String, // 성인 가능 여부
    val isYoungPossible: String, // 청소년 가능 여부
    val field: String, // 분야
    val place: String,
    val sHour: Int,
    val eHour: Int,
    val nsDate: String, // 모집 시작일
    val neDate: String, // 모집 종료일
    val num: Int,
    val manager: String,
    val tel: String,
    val email: String,
    val contents: String,
    val address: String,
) {
    fun toVolunteerInfo(url: String) = this.run {
        VolunteerInfo(
            pID = pID,
            title= title,
            host = host,
            sDate = sDate,
            eDate = eDate,
            state = state,
            url = url,
        )
    }
}