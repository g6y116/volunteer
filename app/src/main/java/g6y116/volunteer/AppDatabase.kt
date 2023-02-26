package g6y116.volunteer

import androidx.room.Database
import androidx.room.RoomDatabase
import g6y116.volunteer.dao.BookMarkDao
import g6y116.volunteer.dao.ReadDao
import g6y116.volunteer.data.Read
import g6y116.volunteer.data.VolunteerInfo

@Database(entities = [VolunteerInfo::class, Read::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBookMarkDao(): BookMarkDao
    abstract fun getReadDao(): ReadDao
}