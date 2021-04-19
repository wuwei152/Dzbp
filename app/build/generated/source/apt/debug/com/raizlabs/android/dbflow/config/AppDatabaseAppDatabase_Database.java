package com.raizlabs.android.dbflow.config;

import com.md.dzbp.data.AppDatabase;
import com.md.dzbp.data.Migration3;
import com.md.dzbp.data.SignBean_Table;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class AppDatabaseAppDatabase_Database extends DatabaseDefinition {
  public AppDatabaseAppDatabase_Database(DatabaseHolder holder) {
    addModelAdapter(new SignBean_Table(this), holder);
    addMigration(4, new Migration3(com.md.dzbp.data.SignBean.class));
  }

  @Override
  public final Class<?> getAssociatedDatabaseClassFile() {
    return AppDatabase.class;
  }

  @Override
  public final boolean isForeignKeysSupported() {
    return false;
  }

  @Override
  public final boolean backupEnabled() {
    return false;
  }

  @Override
  public final boolean areConsistencyChecksEnabled() {
    return false;
  }

  @Override
  public final int getDatabaseVersion() {
    return 4;
  }

  @Override
  public final String getDatabaseName() {
    return "AppDatabase";
  }
}
