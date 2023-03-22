package g6y116.volunteer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import g6y116.volunteer.AppDatabase
import g6y116.volunteer.Const
import g6y116.volunteer.api.KakaoApi
import g6y116.volunteer.api.VolunteerApi
import g6y116.volunteer.dao.BookmarkDao
import g6y116.volunteer.dao.VisitDao
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier annotation class OpenApi
    @Qualifier annotation class Kakao

    @Singleton @Provides
    fun provideBookmarkDao(database: AppDatabase): BookmarkDao = database.getBookmarkDao()

    @Singleton @Provides
    fun provideVisitDao(database: AppDatabase): VisitDao = database.getVisitDao()

    @Singleton @Provides
    fun provideVolunteerApi(@OpenApi retrofit: Retrofit): VolunteerApi = retrofit.create(VolunteerApi::class.java)

    @Singleton @Provides
    fun provideKakaoApi(@Kakao retrofit: Retrofit): KakaoApi = retrofit.create(KakaoApi::class.java)

    @Singleton @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context.applicationContext, AppDatabase::class.java, Const.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton @Provides @OpenApi
    fun provideRetrofit(): Retrofit {
        val xmlParser = TikXml.Builder().exceptionOnUnreadXml(false).build()
        return Retrofit
            .Builder()
            .addConverterFactory(TikXmlConverterFactory.create(xmlParser))
            .baseUrl(Const.OPEN_API_URL)
            .build()
    }

    @Singleton @Provides
    fun provideKakaoInterceptor (): Interceptor {
        return Interceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("Authorization", Const.KAKAO_KEY)
                    .build()
            )
        }
    }

    @Singleton @Provides
    fun provideKakaoOKHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(provideKakaoInterceptor())
            .build()
    }

    @Singleton @Provides @Kakao
    fun provideKakaoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(Const.KAKAO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(Const.DATASTORE_NAME) })
    }
}