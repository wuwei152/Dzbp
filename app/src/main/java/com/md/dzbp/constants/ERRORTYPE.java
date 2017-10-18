package com.md.dzbp.constants;

/**
 * 错误及提示信息
 * Created by Administrator on 2017/9/18.
 */
public interface ERRORTYPE {
    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";

    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";

    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";

    public static final int CARD_SUCCESS = 0;//成功
    public static final int CARD_FAIL = 1;//失败
    public static final int CARD_UNAUTHORIZED = 2;//未授权
    public static final int CARD_NOTFOUND = 3;//无人员信息
    public static final int CARD_NOT_WECHAT = 4;//未关注微信
    public static final int CARD_SAVEEXCEPTION = 5;//保存数据异常

}
