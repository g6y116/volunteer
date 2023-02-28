package g6y116.volunteer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.data.Visit
import androidx.lifecycle.LiveData

@Dao
interface VisitDao {
    @Query("SELECT * FROM visit")
    fun getLiveList(): LiveData<List<Visit>>

    @Query("SELECT * FROM visit")
    fun getList(): List<Visit>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(visit: Visit)

    @Query("DELETE FROM visit")
    suspend fun remove()
}