package com.md.dzbp.data;

import android.content.ContentValues;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.saveable.AutoIncrementModelSaver;
import com.raizlabs.android.dbflow.sql.saveable.ModelSaver;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.Number;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class SignBean_Table extends ModelAdapter<SignBean> {
  /**
   * Primary Key AutoIncrement */
  public static final Property<Long> id = new Property<Long>(SignBean.class, "id");

  public static final Property<String> CarNum = new Property<String>(SignBean.class, "CarNum");

  public static final Property<String> AttendanceTime = new Property<String>(SignBean.class, "AttendanceTime");

  public static final Property<String> FileName = new Property<String>(SignBean.class, "FileName");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{id,CarNum,AttendanceTime,FileName};

  public SignBean_Table(DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
  }

  @Override
  public final Class<SignBean> getModelClass() {
    return SignBean.class;
  }

  @Override
  public final String getTableName() {
    return "`SignBean`";
  }

  @Override
  public final SignBean newInstance() {
    return new SignBean();
  }

  @Override
  public final Property getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch ((columnName)) {
      case "`id`":  {
        return id;
      }
      case "`CarNum`":  {
        return CarNum;
      }
      case "`AttendanceTime`":  {
        return AttendanceTime;
      }
      case "`FileName`":  {
        return FileName;
      }
      default: {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }

  @Override
  public final void updateAutoIncrement(SignBean model, Number id) {
    model.id = id.longValue();
  }

  @Override
  public final Number getAutoIncrementingId(SignBean model) {
    return model.id;
  }

  @Override
  public final String getAutoIncrementingColumnName() {
    return "id";
  }

  @Override
  public final ModelSaver<SignBean> createSingleModelSaver() {
    return new AutoIncrementModelSaver<>();
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return ALL_COLUMN_PROPERTIES;
  }

  @Override
  public final void bindToInsertValues(ContentValues values, SignBean model) {
    values.put("`CarNum`", model.CarNum);
    values.put("`AttendanceTime`", model.AttendanceTime);
    values.put("`FileName`", model.FileName);
  }

  @Override
  public final void bindToContentValues(ContentValues values, SignBean model) {
    values.put("`id`", model.id);
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, SignBean model, int start) {
    statement.bindStringOrNull(1 + start, model.CarNum);
    statement.bindStringOrNull(2 + start, model.AttendanceTime);
    statement.bindStringOrNull(3 + start, model.FileName);
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, SignBean model) {
    int start = 0;
    statement.bindLong(1 + start, model.id);
    bindToInsertStatement(statement, model, 1);
  }

  @Override
  public final void bindToUpdateStatement(DatabaseStatement statement, SignBean model) {
    statement.bindLong(1, model.id);
    statement.bindStringOrNull(2, model.CarNum);
    statement.bindStringOrNull(3, model.AttendanceTime);
    statement.bindStringOrNull(4, model.FileName);
    statement.bindLong(5, model.id);
  }

  @Override
  public final void bindToDeleteStatement(DatabaseStatement statement, SignBean model) {
    statement.bindLong(1, model.id);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `SignBean`(`CarNum`,`AttendanceTime`,`FileName`) VALUES (?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `SignBean`(`id`,`CarNum`,`AttendanceTime`,`FileName`) VALUES (?,?,?,?)";
  }

  @Override
  public final String getUpdateStatementQuery() {
    return "UPDATE `SignBean` SET `id`=?,`CarNum`=?,`AttendanceTime`=?,`FileName`=? WHERE `id`=?";
  }

  @Override
  public final String getDeleteStatementQuery() {
    return "DELETE FROM `SignBean` WHERE `id`=?";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `SignBean`(`id` INTEGER PRIMARY KEY AUTOINCREMENT, `CarNum` TEXT, `AttendanceTime` TEXT, `FileName` TEXT)";
  }

  @Override
  public final void loadFromCursor(FlowCursor cursor, SignBean model) {
    model.id = cursor.getLongOrDefault("id");
    model.CarNum = cursor.getStringOrDefault("CarNum");
    model.AttendanceTime = cursor.getStringOrDefault("AttendanceTime");
    model.FileName = cursor.getStringOrDefault("FileName");
  }

  @Override
  public final boolean exists(SignBean model, DatabaseWrapper wrapper) {
    return model.id > 0
    && SQLite.selectCountOf()
    .from(SignBean.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final OperatorGroup getPrimaryConditionClause(SignBean model) {
    OperatorGroup clause = OperatorGroup.clause();
    clause.and(id.eq(model.id));
    return clause;
  }
}
