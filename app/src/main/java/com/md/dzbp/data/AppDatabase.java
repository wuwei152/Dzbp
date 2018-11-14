package com.md.dzbp.data;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wuwei on 2018/11/1.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";

    public static final int VERSION = 4;
}
