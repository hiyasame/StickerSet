package team.redrock.stickerset.main.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import team.redrock.rain.lib.common.BaseApp
import team.redrock.stickerset.main.model.database.dao.StickerSetDao
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity

@Database(entities = [StickerEntity::class, StickerSetEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stickerSetDao(): StickerSetDao

    companion object {

        val INSTANCE by lazy { create(BaseApp.appContext) }

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "sticker_set.db")
                .build()
    }
}