package g6y116.volunteer.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.data.Read

@Dao
interface ReadDao {
    @Query("SELECT * FROM volunteer_read")
    fun getReadLiveList(): LiveData<List<Read>>

    @Query("SELECT * FROM volunteer_read")
    fun getReadList(): List<Read>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRead(volunteerInfo: Read)

    @Query("DELETE FROM volunteer_read")
    suspend fun removeRead()
}