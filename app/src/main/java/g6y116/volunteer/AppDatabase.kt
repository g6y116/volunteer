package g6y116.volunteer

import androidx.room.Database
import androidx.room.RoomDatabase
import g6y116.volunteer.dao.VolunteerDao
import g6y116.volunteer.data.VolunteerInfo

@Database(entities = [VolunteerInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVolunteerDao(): VolunteerDao
}