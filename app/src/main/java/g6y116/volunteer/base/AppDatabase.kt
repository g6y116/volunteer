package g6y116.volunteer.base

import androidx.room.Database
import androidx.room.RoomDatabase
import g6y116.volunteer.feature.data.datasource.InfoLocalSource
import g6y116.volunteer.feature.data.datasource.VisitLocalSource
import g6y116.volunteer.feature.data.model.Info
import g6y116.volunteer.feature.data.model.Visit

@Database(entities = [Info::class, Visit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getInfoLocalSource(): InfoLocalSource
    abstract fun getVisitLocalSource(): VisitLocalSource
}