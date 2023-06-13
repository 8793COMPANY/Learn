package com.learn4.data.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.learn4.data.room.dao.BlockDictionaryDao;
import com.learn4.data.room.dao.ContentsGoalDao;
import com.learn4.data.room.dao.TestEntityDao;
import com.learn4.data.room.entity.BlockDictionary;
import com.learn4.data.room.entity.ContentGoal;
import com.learn4.data.room.entity.TestEntity;

@Database(entities = {BlockDictionary.class, ContentGoal.class/*, TestEntity.class*/}, version = 2)
public abstract class AppDatabase2 extends RoomDatabase {
    public abstract BlockDictionaryDao blockDictionaryDao();
    public abstract ContentsGoalDao contentsGoalDao();

    //public abstract TestEntityDao testEntityDao();

    private static AppDatabase2 INSTANCE = null;

    static final Migration MIGRATION_1_TO_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `ContentGoal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "`main_category` TEXT, " + "`sub_category` TEXT, "+ "`total_category` TEXT, "
                    + "`category_goal` TEXT, " + "`category_condition` TEXT)");
            //+ "`category_goal` TEXT, " + "`category_condition` TEXT, PRIMARY KEY(`id`))");
        }
    };

    //_db.execSQL("CREATE TABLE IF NOT EXISTS `ContentGoal` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `main_category` TEXT, `sub_category` TEXT, `total_category` TEXT, `category_goal` TEXT, `category_condition` TEXT)");

    static final Migration MIGRATION_2_TO_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `TestEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "`main_category` TEXT, " + "`sub_category` TEXT, "+ "`total_category` TEXT, "
                    + "`category_goal` TEXT, " + "`category_condition` TEXT)");
        }
    };

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static AppDatabase2 getInstance(Context context) {
        if(INSTANCE == null)
        {
            synchronized (AppDatabase2.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase2.class, "blockDB")
                        .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                        .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                        .addMigrations(MIGRATION_1_TO_2/*, MIGRATION_2_TO_3*/)
                        .build();
            }
        }

        return INSTANCE;
    }
}
