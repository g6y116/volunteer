package g6y116.volunteer.feature.data.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import g6y116.volunteer.feature.data.model.Visit
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitLocalSource {

    @Query("SELECT * FROM visit")
    fun load(): Flow<List<Visit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: Visit)

    @Query("DELETE FROM visit")
    suspend fun deleteAll()
}