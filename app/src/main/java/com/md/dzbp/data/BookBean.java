package com.md.dzbp.data;

import java.io.Serializable;

/**
 * {
 *     "msg": "ok",
 *     "data": [
 *         {
 *             "phone_mob": "",
 *             "holiday_id": "0",
 *             "tsg_name": "武汉市实验学校",
 *             "price_sum": "48.11", // 套价
 *             "real_name": "常晨", // 学生姓名
 *             "title": "资治通鉴 八", // 书名
 *             "op_user": "admin",
 *             "tsg_site_code": "999001", // 图书藏址代码
 *             "ltype_code": "LT001", //图书流通类型代码
 *             "lend_status": "2", //图书流通状态， 1正在借出；2：已归还
 *             "dz_type_name": "学生", //读者类型
 *             "tsg_code_re": "999", // 图书馆代码（归还时）
 *             "is_lend_out": false,
 *             "price": "48.11", // 单本价格
 *             "is_out": false,
 *             "re_cnt": "0", //该借阅记录的续借次数
 *             "barcode": "1004382", //文献条码
 *             "dz_id": "500", // 读者ID（系统唯一码）
 *             "email": "",
 *             "must_time": "2020-12-04", // 应还时间
 *             "is_inter_lend": "0", //是否官际借阅 0否  1是
 *             "tsg_code": "999", // 图书管编码（借出时）
 *             "calino": "K204/2:8", // 图书索引号
 *             "dck_id": "4114", // 典藏库自动id(图书唯一码)
 *             "is_other_tsg": "0",
 *             "end_time": "2020-11-04", // 归还日期
 *             "ltype_name": "中文图书",
 *             "tsg_name_re": "武汉市实验学校", // 学校名称
 *             "book_id": "1216",
 *             "site_name": "中文书库", // 馆藏地点
 *             "unit_name": "高二年级3班", // 班级
 *             "dz_code": "3277987478", // 读者证号
 *             "dz_type_code": "XS", // 读者证类型 XS代表学生； JS代表教师 OTH代表其他
 *             "add_time": "2020-11-04" // 借出日期
 *         },
 *  ]
 */


public class BookBean implements Serializable {

    private String phone_mob;
    private String holiday_id;
    private String tsg_name;
    private String price_sum;
    private String real_name;
    private String title;
    private String op_user;
    private String tsg_site_code;
    private String ltype_code;
    private String lend_status;
    private String dz_type_name;
    private String tsg_code_re;
    private boolean is_lend_out;
    private String price;
    private boolean is_out;
    private String re_cnt;
    private String barcode;
    private String dz_id;
    private String email;
    private String must_time;
    private String is_inter_lend;
    private String tsg_code;
    private String calino;
    private String dck_id;
    private String is_other_tsg;
    private String end_time;
    private String ltype_name;
    private String tsg_name_re;
    private String book_id;
    private String site_name;
    private String unit_name;
    private String dz_code;
    private String dz_type_code;
    private String add_time;

    public BookBean() {
    }

    public BookBean(String title, String lend_status, String price, String end_time, String add_time) {
        this.title = title;
        this.lend_status = lend_status;
        this.price = price;
        this.end_time = end_time;
        this.add_time = add_time;
    }

    public String getPhone_mob() {
        return phone_mob;
    }

    public void setPhone_mob(String phone_mob) {
        this.phone_mob = phone_mob;
    }

    public String getHoliday_id() {
        return holiday_id;
    }

    public void setHoliday_id(String holiday_id) {
        this.holiday_id = holiday_id;
    }

    public String getTsg_name() {
        return tsg_name;
    }

    public void setTsg_name(String tsg_name) {
        this.tsg_name = tsg_name;
    }

    public String getPrice_sum() {
        return price_sum;
    }

    public void setPrice_sum(String price_sum) {
        this.price_sum = price_sum;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOp_user() {
        return op_user;
    }

    public void setOp_user(String op_user) {
        this.op_user = op_user;
    }

    public String getTsg_site_code() {
        return tsg_site_code;
    }

    public void setTsg_site_code(String tsg_site_code) {
        this.tsg_site_code = tsg_site_code;
    }

    public String getLtype_code() {
        return ltype_code;
    }

    public void setLtype_code(String ltype_code) {
        this.ltype_code = ltype_code;
    }

    public String getLend_status() {
        return lend_status;
    }

    public void setLend_status(String lend_status) {
        this.lend_status = lend_status;
    }

    public String getDz_type_name() {
        return dz_type_name;
    }

    public void setDz_type_name(String dz_type_name) {
        this.dz_type_name = dz_type_name;
    }

    public String getTsg_code_re() {
        return tsg_code_re;
    }

    public void setTsg_code_re(String tsg_code_re) {
        this.tsg_code_re = tsg_code_re;
    }

    public boolean isIs_lend_out() {
        return is_lend_out;
    }

    public void setIs_lend_out(boolean is_lend_out) {
        this.is_lend_out = is_lend_out;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isIs_out() {
        return is_out;
    }

    public void setIs_out(boolean is_out) {
        this.is_out = is_out;
    }

    public String getRe_cnt() {
        return re_cnt;
    }

    public void setRe_cnt(String re_cnt) {
        this.re_cnt = re_cnt;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDz_id() {
        return dz_id;
    }

    public void setDz_id(String dz_id) {
        this.dz_id = dz_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMust_time() {
        return must_time;
    }

    public void setMust_time(String must_time) {
        this.must_time = must_time;
    }

    public String getIs_inter_lend() {
        return is_inter_lend;
    }

    public void setIs_inter_lend(String is_inter_lend) {
        this.is_inter_lend = is_inter_lend;
    }

    public String getTsg_code() {
        return tsg_code;
    }

    public void setTsg_code(String tsg_code) {
        this.tsg_code = tsg_code;
    }

    public String getCalino() {
        return calino;
    }

    public void setCalino(String calino) {
        this.calino = calino;
    }

    public String getDck_id() {
        return dck_id;
    }

    public void setDck_id(String dck_id) {
        this.dck_id = dck_id;
    }

    public String getIs_other_tsg() {
        return is_other_tsg;
    }

    public void setIs_other_tsg(String is_other_tsg) {
        this.is_other_tsg = is_other_tsg;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLtype_name() {
        return ltype_name;
    }

    public void setLtype_name(String ltype_name) {
        this.ltype_name = ltype_name;
    }

    public String getTsg_name_re() {
        return tsg_name_re;
    }

    public void setTsg_name_re(String tsg_name_re) {
        this.tsg_name_re = tsg_name_re;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getDz_code() {
        return dz_code;
    }

    public void setDz_code(String dz_code) {
        this.dz_code = dz_code;
    }

    public String getDz_type_code() {
        return dz_type_code;
    }

    public void setDz_type_code(String dz_type_code) {
        this.dz_type_code = dz_type_code;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
