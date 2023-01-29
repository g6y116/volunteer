package g6y116.volunteer

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [VolunteerInfo::class, Volunteer::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getVolunteerInfoDao(): VolunteerInfoDao
    abstract fun getVolunteerDao(): VolunteerDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(application : Application): AppDatabase? {
            if (instance == null) {
                synchronized(this){
                    instance = Room.databaseBuilder(application, AppDatabase::class.java, Const.DB_NAME).fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}