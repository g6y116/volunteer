package g6y116.volunteer

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g6y116.volunteer.data.Volunteer
import g6y116.volunteer.data.VolunteerInfo
import g6y116.volunteer.repository.*
import retrofit2.Retrofit
import javax.inject.Singleton

@HiltAndroidApp
class AppApplication : Application() {

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    // Repository
    @Singleton
    @Binds
    abstract fun bindVolunteerRepository(
        repositoryImpl: VolunteerRepositoryImpl
    ): VolunteerRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // DAO
    @Singleton
    @Provides
    fun provideVolunteerInfoDao(database: AppDatabase): VolunteerInfoDao {
        return database.getVolunteerInfoDao()
    }

    // Api
    @Singleton
    @Provides
    fun provideVolunteerApi(retrofit: Retrofit): VolunteerApi {
        return retrofit.create(VolunteerApi::class.java)
    }

    // Room DataBase
    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, Const.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    // Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val xmlParser = TikXml.Builder().exceptionOnUnreadXml(false).build()

        return Retrofit.Builder()
            .addConverterFactory(TikXmlConverterFactory.create(xmlParser))
            .baseUrl(Const.SERVER_URL)
            .build()
    }

    // DataStore
//    @Singleton
//    @Provides
//    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        PreferenceDataStoreFactory.create {
//            produceFile = { context.preferencesDataStoreFile(Const.DATASTORE_NAME) }
//        }
//    }
}

// DataBase
@Database(entities = [VolunteerInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getVolunteerInfoDao(): VolunteerInfoDao
}