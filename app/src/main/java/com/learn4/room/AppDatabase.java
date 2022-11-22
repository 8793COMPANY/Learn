package com.learn4.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learn4.room.dao.ComponentDao;
import com.learn4.room.dao.ContentsDao;
import com.learn4.room.entity.Component;
import com.learn4.room.entity.Contents;


@Database(entities = {Component.class, Contents.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ComponentDao componentDao();
    public abstract ContentsDao contentsDao();

    private static AppDatabase INSTANCE = null;

    //MovieDatabaseManager 싱글톤 패턴으로 구현
    public static AppDatabase getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "componentDB")
                        .fallbackToDestructiveMigration() //스키마 버전 변경 가능
                        .allowMainThreadQueries() // 메인 스레드에서 DB에 IO를 가능하게 함
                        .build();
            }
        }

        return INSTANCE;
    }
}
