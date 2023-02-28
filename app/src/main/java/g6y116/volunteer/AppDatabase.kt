package g6y116.volunteer

import androidx.room.Database
import androidx.room.RoomDatabase
import g6y116.volunteer.dao.BookmarkDao
import g6y116.volunteer.dao.VisitDao
import g6y116.volunteer.data.Info
import g6y116.volunteer.data.Visit

@Database(entities = [Info::class, Visit::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBookmarkDao(): BookmarkDao
    abstract fun getVisitDao(): VisitDao
}