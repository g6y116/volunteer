package g6y116.volunteer.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.data.VolunteerInfo

@Dao
interface VolunteerDao {
    @Query("SELECT * FROM volunteer_info")
    fun getVolunteerLiveList(): LiveData<List<VolunteerInfo>>

    @Query("SELECT * FROM volunteer_info")
    fun getVolunteerList(): List<VolunteerInfo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addVolunteer(volunteerInfo: VolunteerInfo)

    @Query("DELETE FROM volunteer_info WHERE p_id = :pID")
    suspend fun removeVolunteer(pID: String)
}