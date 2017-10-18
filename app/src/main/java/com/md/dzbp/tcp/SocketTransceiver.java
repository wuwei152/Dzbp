package com.md.dzbp.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import android.util.Log;

/**
 * Socket收发器 通过Socket发送数据，并使用新线程监听Socket接收到的数据
 *
 */
public abstract class SocketTransceiver implements Runnable {

	protected Socket socket;
	protected InetAddress addr;
	protected InputStream in;
	protected OutputStream out;
	private boolean runFlag;

	/**
	 * 实例化
	 *
	 * @param socket
	 *            已经建立连接的socket
	 */
	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	/**
	 * 获取连接到的Socket地址
	 *
	 * @return InetAddress对象
	 */
	public InetAddress getInetAddress() {
		return addr;
	}

	/**
	 * 开启Socket收发
	 * <p>
	 * 如果开启失败，会断开连接并回调{@code onDisconnect()}
	 */
	public void start() {
		runFlag = true;
		new Thread(this).start();
	}

	/**
	 * 断开连接(主动)
	 * <p>
	 * 连接断开后，会回调{@code onDisconnect()}
	 */
	public void stop() {
		runFlag = false;
		try {
			socket.shutdownInput();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送字符串
	 *
	 * @return 发送成功返回true
	 */
	public boolean send(TCPMessage message){
		if (out != null) {
			try {
				// out.writeUTF(s);
				// out.flush();

				byte[] data = message.GetByte();
				out.write(data);
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				runFlag = false;
			}
		}
		return false;
	}

	/**
	 * 监听Socket接收的数据(新线程中运行)
	 */
	@Override
	public void run() {
		DatagramR r = new DatagramR();
		try {
			in = this.socket.getInputStream();
			out = this.socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("socket", e.getMessage());
			runFlag = false;
		}
		while (runFlag) {
			try {

				// final String s = in.readUTF();
				// this.onReceive(addr, s);

				byte[] buff = new byte[2048];
				in.read(buff);
				List<TCPMessage> messageList = r.Resolve(buff);
//				Log.i("socket", "共解析到" + messageList.size() + "条消息");

				this.onReceive(addr, messageList);
			} catch (IOException e) {
				// 连接被断开(被动)
				Log.e("Socket", e.getMessage());
				runFlag = false;
			}
		}

		// 断开连接
		try {
			in.close();
			out.close();
			socket.close();
			in = null;
			out = null;
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.onDisconnect(addr);
	}

	/**
	 * 接收到数据
	 * <p>
	 * 注意：此回调是在新线程中执行的
	 *
	 * @param addr
	 *            连接到的Socket地址
	 * @param messageList
	 *            收到的字符串
	 */
	public abstract void onReceive(InetAddress addr,
								   List<TCPMessage> messageList);

	/**
	 * 连接断开
	 * <p>
	 * 注意：此回调是在新线程中执行的
	 *
	 * @param addr
	 *            连接到的Socket地址
	 */
	public abstract void onDisconnect(InetAddress addr);
}