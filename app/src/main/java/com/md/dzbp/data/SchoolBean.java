package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/12.
 */
public class SchoolBean implements Serializable {


    /**
     * schoolid : 01746730-8e64-11e5-8baa-1c6f6576b1d6
     * schoolname : 罗汉中学
     */

    private String schoolid;
    private String schoolname;

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }
}
