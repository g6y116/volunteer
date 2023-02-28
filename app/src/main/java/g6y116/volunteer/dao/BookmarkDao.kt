package g6y116.volunteer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.data.Info
import androidx.lifecycle.LiveData

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM info")
    fun getLiveList(): LiveData<List<Info>>

    @Query("SELECT * FROM info")
    fun getList(): List<Info>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(info: Info)

    @Query("DELETE FROM info WHERE p_id = :pID")
    suspend fun remove(pID: String)
}