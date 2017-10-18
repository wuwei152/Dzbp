package com.md.dzbp.tcp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import android.util.Log;

/**
 * byte数组转换工具类
 */
public class DigitalUtils {

	/**
	 * 将长度为4的byte数组转换为16位int 大端字序
	 *
	 * @param res
	 *            byte[]
	 * @return int
	 */
	public static int byte2Int(byte[] res, int index) {
		return (res[index] & 0xff) | ((res[index + 1] << 8) & 0xff00)
				| ((res[index + 2] << 16) & 0xff0000)
				| ((res[index + 3] << 24) & 0xff000000); // 表示安位或
	}

	/**
	 * 将int 转为四个字节的byte
	 *
	 * @param i
	 * @return
	 */
	public static byte[] int2Byte(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[3] = (byte) ((i >> 24) & 0xFF);
		return result;
	}

	/**
	 * long to byte[]
	 *
	 * @param s
	 *            long
	 * @return byte[]
	 * */
	public static byte[] longToByte(long s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 8; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	// double转换为byte[8]数组
	public static byte[] double2byte(double d) {
		long longbits = Double.doubleToLongBits(d);
		return getByteArray(longbits);
	}

	public static Double byte2double(byte[] data, int index) {
		return Double.longBitsToDouble(getLong(data, index));
	}

	/**
	 * 将一个4byte的数组转换成32位的int
	 *
	 * @param buf
	 *            bytes buffer
	 * @param byte[]中开始转换的位置
	 * @return convert result
	 */
	public static long byte2long(byte[] buf, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) buf[index]));
		secondByte = (0x000000FF & ((int) buf[index + 1]));
		thirdByte = (0x000000FF & ((int) buf[index + 2]));
		fourthByte = (0x000000FF & ((int) buf[index + 3]));
		index = index + 4;
		return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	public static byte[] float2byte(float f) {
		int intbits = Float.floatToIntBits(f);// 将float里面的二进制串解释为int整数
		return getByteArray(intbits);
	}

	public static float byte2float(byte[] buf, int index) {
		return Float.intBitsToFloat(getInt(buf, index));
	}

	// long转换为byte[8]数组
	public static byte[] getByteArray(long l) {
		byte b[] = new byte[8];
		b[0] = (byte) (0xff & (l >> 56));
		b[1] = (byte) (0xff & (l >> 48));
		b[2] = (byte) (0xff & (l >> 40));
		b[3] = (byte) (0xff & (l >> 32));
		b[4] = (byte) (0xff & (l >> 24));
		b[5] = (byte) (0xff & (l >> 16));
		b[6] = (byte) (0xff & (l >> 8));
		b[7] = (byte) (0xff & l);
		return b;
	}

	// 从byte数组的index处的连续4个字节获得一个int
	public static int getInt(byte[] arr, int index) {
		return (0xff000000 & (arr[index + 0] << 24))
				| (0x00ff0000 & (arr[index + 1] << 16))
				| (0x0000ff00 & (arr[index + 2] << 8))
				| (0x000000ff & arr[index + 3]);
	}

	public static long getLong(byte[] arr, int index) {
		return (0xff00000000000000L & ((long) arr[index + 0] << 56))
				| (0x00ff000000000000L & ((long) arr[index + 1] << 48))
				| (0x0000ff0000000000L & ((long) arr[index + 2] << 40))
				| (0x000000ff00000000L & ((long) arr[index + 3] << 32))
				| (0x00000000ff000000L & ((long) arr[index + 4] << 24))
				| (0x0000000000ff0000L & ((long) arr[index + 5] << 16))
				| (0x000000000000ff00L & ((long) arr[index + 6] << 8))
				| (0x00000000000000ffL & (long) arr[index + 7]);
	}

	/**
	 * 将byte数组转换为字符串
	 *
	 * @param src
	 * @return
	 */
	public static String byte2String(byte[] src) {
		String gbk = "";
		try {
			gbk = new String(src, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return gbk;
	}

	/**
	 * 将short转换为byte数组
	 *
	 * @param s
	 *            short
	 * @return
	 */
	public static byte[] short2Byte(int s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[1 - i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	/**
	 * 将长度为2的byte数组转换为16位int
	 *
	 * @param res
	 *            byte[]
	 * @return int
	 */
	private static int byte2Short(byte[] res) {
		return (res[0] & 0xff) | ((res[1] << 8) & 0xff00); // | 表示安位或
	}

	/**
	 * 将长度为2的byte数组转换为16位int
	 *
	 * @param res
	 *            byte[]
	 * @return int
	 */
	public static short byte2Short(byte[] res, int start) {
		int targets = (res[start] & 0xff) | ((res[start + 1] << 8) & 0xff00); // |
		// 表示安位或
		return (short) targets;
	}

	public static char byteToChar(byte[] b) {
		char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
		return c;
	}

	/**
	 * 根据char数组获取byte数组
	 *
	 * @param chars
	 * @return
	 */
	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);

		return bb.array();
	}

	/**
	 * 根据byte数组获取char数组
	 *
	 * @param bytes
	 * @return
	 */
	public static char[] getChars(byte[] bytes) {
		Charset cs = Charset.forName("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}

	/**
	 * @param msg
	 *            需要计算校验和的byte数组(无符号校验和)
	 * @param start
	 *            msg的开始位置
	 * @return 计算出的校验和数组
	 */
	public static short sumCheckShort(byte[] msg, int start, int count) {
		byte[] newByte = new byte[count];
		System.arraycopy(msg, start, newByte, 0, newByte.length);
		int mSum = 0;
		int temp;
		short chksum;
		int i = 0;
		for (; i < newByte.length - 1; i += 2) {
			temp = newByte[i + 1];
			temp <<= 8;
			if (temp < 0) {
				temp += 65536;
			}
			if (newByte[i] < 0) {
				temp += 256;
			}
			temp += newByte[i];
			mSum += temp;
		}
		if (i != newByte.length) {
			if (newByte[newByte.length - 1] < 0) {
				mSum += 256;
			}
			mSum += newByte[newByte.length - 1];
		}
		mSum = (mSum >> 16) + (mSum & 0xffff);
		mSum += (mSum >> 16);
		chksum = (short) ~mSum;
		return chksum;
	}

	/**
	 * 组合byte数组
	 *
	 * @param bs
	 * @return
	 */
	public static byte[] mergeBytes(byte[]... bs) {
		int length = 0;
		for (byte[] bs2 : bs) {
			length += bs2.length;
		}
		// 请求数组长度
		byte[] result = new byte[length];
		int curLength = 0;
		for (byte[] b : bs) {
			System.arraycopy(b, 0, result, curLength, b.length);
			curLength += b.length;
		}
		return result;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv + " ");
		}
		return stringBuilder.toString();
	}

	/**
	 * 将InputStream转换成byte数组
	 *
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] InputStreamTOByte(InputStream in) throws IOException {
		Log.i("InputStreamTOByte", "从流中取出数据");
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[in.available()];
		int n = 0;
		while (-1 != (n = in.read(buffer))) {
			outStream.write(buffer, 0, n);
		}
		Log.i("InputStreamTOByte", "从流中取出数据==完成");
		return outStream.toByteArray();
	}
}
