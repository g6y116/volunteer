package g6y116.volunteer.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.data.VolunteerInfo

@Dao
interface BookMarkDao {
    @Query("SELECT * FROM volunteer_info")
    fun getBookMarkLiveList(): LiveData<List<VolunteerInfo>>

    @Query("SELECT * FROM volunteer_info")
    fun getBookMarkList(): List<VolunteerInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookMark(volunteerInfo: VolunteerInfo)

    @Query("DELETE FROM volunteer_info WHERE p_id = :pID")
    suspend fun removeBookMark(pID: String)
}