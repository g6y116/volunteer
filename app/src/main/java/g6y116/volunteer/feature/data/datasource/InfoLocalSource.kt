package g6y116.volunteer.feature.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.feature.data.model.Info
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoLocalSource {

    @Query("SELECT * FROM info")
    fun load(): Flow<List<Info>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(info: Info)

    @Query("DELETE FROM info WHERE p_id = :pID")
    suspend fun delete(pID: String)
}