package com.example.madlevel4task2

import android.content.Context
import androidx.room.*
import java.util.*

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(GameRoomDatabase.Converters::class)
abstract class GameRoomDatabase : RoomDatabase() {
    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time?.toLong()
        }
    }

    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "GAMES_DATABASE"

        private var gameRoomDatabaseInstance: GameRoomDatabase? = null
        fun getDatabase(context: Context): GameRoomDatabase? {
            if (gameRoomDatabaseInstance == null) {
                gameRoomDatabaseInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        GameRoomDatabase::class.java,
                        DATABASE_NAME
                    ).build()
            }
            return gameRoomDatabaseInstance
        }
    }
}