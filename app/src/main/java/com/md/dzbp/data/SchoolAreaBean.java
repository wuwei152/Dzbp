package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/12.
 */
public class SchoolAreaBean implements Serializable{

    /**
     * address : A3栋101室
     * id : a132db64-ffe1-4263-ba77-599c5104ca72
     */

    private String address;
    private String id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
