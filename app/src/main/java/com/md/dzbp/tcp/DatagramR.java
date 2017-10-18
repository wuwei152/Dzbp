package com.md.dzbp.tcp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 接受报文
 */
public class DatagramR {
	public byte[] newDatagram;

	public List<TCPMessage> Resolve(byte[] datagram) {
		newDatagram = datagram;

//		Log.i("DatagramR", "开始解析协议包");
		List<TCPMessage> datalist = new ArrayList<TCPMessage>();

		Boolean isover = false;
		while (!isover) {
			// 保证报文开头是"{"符号,否则将报文清空
			if (datagram[0] == 123) {
				// 读出消息结构体
				TCPMessage msg = new TCPMessage();
				short bigsize = 0;
				short datagramcount = 10;

				try {
					if (datagram.length >= 2 + 1) {
						msg.Type = DigitalUtils.byte2Int(datagram, 1);
						if (datagram.length >= datagramcount + 1) {
							msg.SN = DigitalUtils.byte2Short(datagram, 3);
							bigsize = DigitalUtils.byte2Short(datagram, 7);
						} else {
							isover = true;
						}
					} else {
						isover = true;
					}
				} catch (Exception e) {
					datagram = null;
					isover = true;
				}

				// 保证报文是指定长度,否则作为下次循环
				if (isover != true
						&& datagram.length >= datagramcount + bigsize) {
					// 保证最后一位是"}"结束符号,否则将报文清空
					if (datagram[datagramcount - 1 + bigsize] == 125) {
						msg.Buff = new byte[bigsize];
						System.arraycopy(datagram, datagramcount - 1, msg.Buff,
								0, bigsize);
						// 加入到集合datalist中
						datalist.add(msg);
						msg.Size(bigsize);
						// 报文是否带有粘包现象,如果存在,保存下来作为下次解析用.
						if (datagram.length > datagramcount + bigsize) {
							// 剩余长度
							int oldsize = datagram.length
									- (datagramcount + bigsize);
							byte[] oldDatagram = new byte[oldsize];
							System.arraycopy(datagram,
									(datagramcount + bigsize), oldDatagram, 0,
									oldsize);
							datagram = oldDatagram;
							newDatagram = oldDatagram;
							// 这里作为下一个循环的入口
						} else {
							datagram = null;
							isover = true;
						}
					} else {
						datagram = null;
						isover = true;
					}
				} else {
					isover = true;
				}
			} else {
				datagram = null;
				isover = true;
			}
		}

		return datalist;
	}
}
