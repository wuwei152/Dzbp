package com.md.dzbp.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * 离线考勤
 * Created by Administrator on 2018/11/1.
 */
@Table(database = AppDatabase.class)
public class SignBean extends BaseModel implements Serializable{
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String CarNum;
    @Column
    public String AttendanceTime;
    @Column
    public String FileName;

    public SignBean() {
    }

    public SignBean(String carNum, String attendanceTime, String FileName) {
        this.CarNum = carNum;
        this.AttendanceTime = attendanceTime;
        this.FileName = FileName;
    }

}

