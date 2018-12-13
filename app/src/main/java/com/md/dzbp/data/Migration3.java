package com.md.dzbp.data;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by Administrator on 2018/11/13.
 */

@Migration(version = 4, database = AppDatabase.class)
public  class Migration3 extends AlterTableMigration<SignBean> {

    public Migration3(Class<SignBean> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        addColumn(SQLiteType.TEXT, "FileName");
    }
}