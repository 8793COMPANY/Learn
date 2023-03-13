package com.learn4.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learn4.data.room.dao.BlockDictionaryDao;
import com.learn4.data.room.entity.BlockDictionary;

@Database(entities = {BlockDictionary.class}, version = 1)
public abstract class AppDatabase2 extends RoomDatabase {
    public abstract BlockDictionaryDao blockDictionaryDao();

    private static AppDatabase2 INSTANCE = null;

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static AppDatabase2 getInstance(Context context) {
        if(INSTANCE == null)
        {
            synchronized (AppDatabase2.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase2.class, "blockDB")
                        .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                        .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                        .build();
            }
        }

        return INSTANCE;
    }
}
