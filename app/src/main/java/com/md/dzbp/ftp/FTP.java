package com.md.dzbp.ftp;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.FtpParams;
import com.md.dzbp.utils.ACache;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class FTP {
    private final Logger logger;
    /**
     * 服务器名.
     */
    private String hostName;

    /**
     * 端口号
     */
    private int serverPort;

    /**
     * 用户名.
     */
    private String userName;

    /**
     * 密码.
     */
    private String password;

    /**
     * FTP连接.
     */

    private FTPClient ftpClient;
    private String TAG = "FTP-->{}";

    public FTP(Context context) {
        ACache mAcache = ACache.get(context);
        logger = LoggerFactory.getLogger(context.getClass());
        FtpParams params = (FtpParams) mAcache.getAsObject("FtpParams");
        if (params != null) {
            LogUtils.d(params);
            this.hostName = params.getIP();
            this.serverPort = params.getPort();
            this.userName = params.getUserName();
            this.password = params.getPsw();
        }
        this.ftpClient = new FTPClient();
    }

    // -------------------------------------------------------文件上传方法------------------------------------------------

    /**
     * 上传单个文件.
     *
     * @param singleFile 本地文件
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadSingleFile(File singleFile, String remotePath,
                                 UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;
        flag = uploadingSingle(singleFile, listener);
        if (flag) {
            listener.onUploadProgress(ERRORTYPE.FTP_UPLOAD_SUCCESS, 0,
                    singleFile);
        } else {
            listener.onUploadProgress(ERRORTYPE.FTP_UPLOAD_FAIL, 0,
                    singleFile);
        }

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传多个文件.
     *
     * @param fileList   本地文件
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    public void uploadMultiFile(LinkedList<File> fileList, String remotePath,
                                UploadProgressListener listener) throws IOException {

        // 上传之前初始化
        this.uploadBeforeOperate(remotePath, listener);

        boolean flag;

        for (File singleFile : fileList) {
            flag = uploadingSingle(singleFile, listener);
            if (flag) {
                listener.onUploadProgress(ERRORTYPE.FTP_UPLOAD_SUCCESS, 0,
                        singleFile);
            } else {
                listener.onUploadProgress(ERRORTYPE.FTP_UPLOAD_FAIL, 0,
                        singleFile);
            }
        }

        // 上传完成之后关闭连接
        this.uploadAfterOperate(listener);
    }

    /**
     * 上传单个文件.
     *
     * @param localFile 本地文件
     * @return true上传成功, false上传失败
     * @throws IOException
     */
    private boolean uploadingSingle(File localFile,
                                    UploadProgressListener listener) throws IOException {
        boolean flag = true;
        // 不带进度的方式
        // // 创建输入流
        // InputStream inputStream = new FileInputStream(localFile);
        // // 上传单个文件
        // flag = ftpClient.storeFile(localFile.getName(), inputStream);
        // // 关闭文件流
        // inputStream.close();

        // 带有进度的方式
        try {
            BufferedInputStream buffIn = new BufferedInputStream(
                    new FileInputStream(localFile));
            ProgressInputStream progressInput = new ProgressInputStream(buffIn, listener, localFile);
            flag = ftpClient.storeFile(localFile.getName(), progressInput);
            logger.debug(TAG, "上传是否成功标志："+flag);
            buffIn.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG, e.getMessage());
        }
        return flag;
    }

    /**
     * 上传文件之前初始化相关参数
     *
     * @param remotePath FTP目录
     * @param listener   监听器
     * @throws IOException
     */
    private void uploadBeforeOperate(String remotePath,
                                     UploadProgressListener listener) throws IOException {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onUploadProgress(ERRORTYPE.FTP_CONNECT_SUCCESSS, 0,
                    null);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onUploadProgress(ERRORTYPE.FTP_CONNECT_FAIL, 0, null);
            return;
        }
        try {
            // 设置模式
            ftpClient.setFileTransferMode(org.apache.commons.net.ftp.FTP.STREAM_TRANSFER_MODE);
            // FTP下创建文件夹
            ftpClient.makeDirectory(remotePath);
            // 改变FTP目录
            ftpClient.changeWorkingDirectory(remotePath);
            // 上传单个文件
        } catch (Exception e) {
            logger.debug(TAG, e.getMessage());
        }
    }

    /**
     * 上传完成之后关闭连接
     *
     * @param listener
     * @throws IOException
     */
    private void uploadAfterOperate(UploadProgressListener listener)
            throws IOException {
        this.closeConnect();
        listener.onUploadProgress(ERRORTYPE.FTP_DISCONNECT_SUCCESS, 0, null);
    }

    // -------------------------------------------------------文件下载方法------------------------------------------------

    /**
     * 下载单个文件，可实现断点下载.
     *
     * @param serverPath Ftp目录及文件路径
     * @param localPath  本地目录
     * @param fileName   下载之后的文件名称
     * @param listener   监听器
     * @throws IOException
     */
    public void downloadSingleFile(String serverPath, String localPath, String fileName, DownLoadProgressListener listener)
            throws Exception {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDownLoadProgress(ERRORTYPE.FTP_CONNECT_SUCCESSS, 0, null);
        } catch (IOException e1) {
            logger.debug("FTP打开", e1.getMessage());
            listener.onDownLoadProgress(ERRORTYPE.FTP_CONNECT_FAIL, 0, null);
            return;
        }
        try {
            // 先判断服务器文件是否存在
            FTPFile[] files = ftpClient.listFiles(serverPath);
            if (files.length == 0) {
                listener.onDownLoadProgress(ERRORTYPE.FTP_FILE_NOTEXISTS, 0, null);
                return;
            }


            //创建本地文件夹
            File mkFile = new File(localPath);
            if (!mkFile.exists()) {
                mkFile.mkdirs();
            }

            localPath = localPath + fileName;
            // 接着判断下载的文件是否能断点下载
            long serverSize = files[0].getSize(); // 获取远程文件的长度
            File localFile = new File(localPath);
            long localSize = 0;
            if (localFile.exists()) {
                localFile.delete();
//                localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
//                if (localSize >= serverSize) {
//                    File file = new File(localPath);
//                    file.delete();
//                }
            }
//            logger.debug(TAG, "下载");
            // 进度
            long step = serverSize / 100;
            long process = 0;
            long currentSize = 0;
            // 开始准备下载文件
            OutputStream out = new FileOutputStream(localFile, true);
            ftpClient.setRestartOffset(localSize);
            InputStream input = ftpClient.retrieveFileStream(serverPath);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = input.read(b)) != -1) {
                out.write(b, 0, length);
                currentSize = currentSize + length;
                if (currentSize / step != process) {
                    process = currentSize / step;
                    if (process % 5 == 0) {  //每隔%5的进度返回一次
                        listener.onDownLoadProgress(ERRORTYPE.FTP_DOWN_LOADING, process, null);
                    }
                }
            }
            out.flush();
            out.close();
            input.close();
            LogUtils.d("下载完成");
            // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
            if (ftpClient.completePendingCommand()) {
                listener.onDownLoadProgress(ERRORTYPE.FTP_DOWN_SUCCESS, 0, new File(localPath));
            } else {
                listener.onDownLoadProgress(ERRORTYPE.FTP_DOWN_FAIL, 0, null);
            }

            // 下载完成之后关闭连接
            this.closeConnect();
            listener.onDownLoadProgress(ERRORTYPE.FTP_DISCONNECT_SUCCESS, 0, null);
        } catch (Exception e) {
            logger.debug("FTP33", e.getMessage());
            listener.onDownLoadProgress(ERRORTYPE.FTP_DOWN_FAIL, 0, null);
        }
        return;
    }

    // -------------------------------------------------------文件删除方法------------------------------------------------

    /**
     * 删除Ftp下的文件.
     *
     * @param serverPath Ftp目录及文件路径
     * @param listener   监听器
     * @throws IOException
     */
    public void deleteSingleFile(String serverPath, DeleteFileProgressListener listener)
            throws Exception {

        // 打开FTP服务
        try {
            this.openConnect();
            listener.onDeleteProgress(ERRORTYPE.FTP_CONNECT_SUCCESSS);
        } catch (IOException e1) {
            e1.printStackTrace();
            listener.onDeleteProgress(ERRORTYPE.FTP_CONNECT_FAIL);
            return;
        }

        // 先判断服务器文件是否存在
        FTPFile[] files = ftpClient.listFiles(serverPath);
        if (files.length == 0) {
            listener.onDeleteProgress(ERRORTYPE.FTP_FILE_NOTEXISTS);
            return;
        }

        //进行删除操作
        boolean flag = true;
        flag = ftpClient.deleteFile(serverPath);
        if (flag) {
            listener.onDeleteProgress(ERRORTYPE.FTP_DELETEFILE_SUCCESS);
        } else {
            listener.onDeleteProgress(ERRORTYPE.FTP_DELETEFILE_FAIL);
        }

        // 删除完成之后关闭连接
        this.closeConnect();
        listener.onDeleteProgress(ERRORTYPE.FTP_DISCONNECT_SUCCESS);

        return;
    }

    // -------------------------------------------------------打开关闭连接------------------------------------------------

    /**
     * 打开FTP服务.
     *
     * @throws IOException
     */
    public void openConnect() throws IOException {
        // 中文转码
        ftpClient.setControlEncoding("UTF-8");
        int reply; // 服务器响应值
        // 连接至服务器
        ftpClient.connect(hostName, serverPort);
        logger.debug(TAG,"------开始连接到ftp///"+hostName+":"+serverPort);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            logger.debug(TAG,"------连接ftp失败------");
            throw new IOException("connect fail: " + reply);
        }else {
            logger.debug(TAG,"------连接ftp成功------");
        }
        // 登录到服务器
        ftpClient.login(userName, password);
        logger.debug(TAG,"------开始登录ftp///"+userName+":"+password);
        // 获取响应值
        reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            // 断开连接
            ftpClient.disconnect();
            logger.debug(TAG,"------登录ftp失败------");
            throw new IOException("connect fail: " + reply);
        } else {
            logger.debug(TAG,"------登录ftp成功------");
            // 获取登录信息
            FTPClientConfig config = new FTPClientConfig(ftpClient
                    .getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);
            // 使用被动模式设为默认
            ftpClient.enterLocalPassiveMode();
            // 二进制文件支持
            ftpClient
                    .setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        }
    }

    /**
     * 关闭FTP服务.
     *
     * @throws IOException
     */
    public void closeConnect() throws IOException {
        if (ftpClient != null) {
            // 退出FTP
            ftpClient.logout();
            // 断开连接
            ftpClient.disconnect();
        }
    }

    // ---------------------------------------------------上传、下载、删除监听---------------------------------------------

    /*
     * 上传进度监听
     */
    public interface UploadProgressListener {
        public void onUploadProgress(String currentStep, long uploadSize, File file);
    }

    /*
     * 下载进度监听
     */
    public interface DownLoadProgressListener {
        public void onDownLoadProgress(String currentStep, long downProcess, File file);
    }

    /*
     * 文件删除监听
     */
    public interface DeleteFileProgressListener {
        public void onDeleteProgress(String currentStep);
    }

}