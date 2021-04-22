package com.md.dzbp.tcp;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * Socket收发器 通过Socket发送数据，并使用新线程监听Socket接收到的数据
 *
 */
public abstract class SocketTransceiver implements Runnable {

	private final Logger logger;
	protected Socket socket;
	protected InetAddress addr;
	protected InputStream in;
	protected OutputStream out;
	private boolean runFlag;

	private byte[] OldDatagram ;
	private String TAG = "SocketTransceiver-->{}";

	/**
	 * 实例化
	 *
	 * @param socket
	 *            已经建立连接的socket
	 */
	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
		logger = LoggerFactory.getLogger(getClass());
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
			logger.error(TAG,e.getMessage());
		}
	}

	/**
	 * 发送字符串
	 *
	 * @return 发送成功返回true
	 */
	public boolean send(TCPMessage message){
		if (out != null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// out.writeUTF(s);
						// out.flush();

						byte[] data = message.GetByte();
						out.write(data);
						out.flush();
//						return true;
					} catch (Exception e) {
						e.printStackTrace();
						logger.error(TAG,e.getMessage());
						runFlag = false;
					}
				}
			}).start();

		}
		return false;
	}

	public byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			logger.debug(TAG+"copy",n);
		}
		return output.toByteArray();
	}

	/**
	 * 监听Socket接收的数据(新线程中运行)
	 */
	@Override
	public void run() {
		//DatagramR r = new DatagramR();
		try {
			in = this.socket.getInputStream();
			out = this.socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(TAG,e.getMessage());
			runFlag = false;
		}
		while (runFlag) {
			try {
				byte[] newDatagram;
				byte[] buffer = new byte[4096];
				int length = in.read(buffer);

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (length > 0) {
					byte[] newBuffer = Arrays.copyOf(buffer, length);

					//logger.debug(TAG + "newLength", newBuffer.length);
					//logger.debug(TAG + "newBufferLength", newBuffer.length);

					if (this.OldDatagram != null && this.OldDatagram.length > 0) {
						newDatagram = new byte[this.OldDatagram.length + newBuffer.length];
						//logger.debug(TAG + "allLength", newDatagram.length);

						byte[] oldBuffer = Arrays.copyOf(this.OldDatagram, this.OldDatagram.length);
						//LogUtils.d(oldBuffer);
						//LogUtils.d(newBuffer);

						//logger.debug(TAG + "oldBufferLength", oldBuffer.length);

						System.arraycopy(oldBuffer, 0, newDatagram, 0, oldBuffer.length);
						System.arraycopy(newBuffer, 0, newDatagram, oldBuffer.length, newBuffer.length);

						//LogUtils.d(newDatagram);
					} else {
						newDatagram = new byte[newBuffer.length];
						newDatagram = Arrays.copyOf(newBuffer, newBuffer.length);
					}

					//logger.debug(TAG + "endLength", newDatagram.length);

					ResolveResult result = DatagramR.Resolve(newDatagram);
					this.OldDatagram = result.OldDatagram;
//					LogUtils.d(result.OldDatagram);

					Log.i("socket", "共解析到" + result.NewMessagelist.size() + "条消息");

					this.onReceive(addr, result.NewMessagelist);
				}
				else
				{
					//LogUtils.d("没有拿到数据");
				}
			} catch (IOException e) {
				// 连接被断开(被动)
				Log.e("Socket", e.getMessage());
				logger.error(TAG,e.getMessage());
				runFlag = false;
				this.OldDatagram = null;
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
			OldDatagram = null;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(TAG,e.getMessage());
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