package com.md.dzbp.data;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */
public class ClassManagerBean {
    /**
     * accountId : 342fa921-572e-42f8-8541-6e53d5e92e4f
     * accountName : 张博
     * subjects : ["英语","数学"]
     * photo : null
     */

    private String accountId;
    private String accountName;
    private String photo;
    private List<String> subjects;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }
}
