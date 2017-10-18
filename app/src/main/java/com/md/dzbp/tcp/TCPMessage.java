package com.md.dzbp.tcp;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import android.util.Log;

public class TCPMessage implements Serializable{
	public int Type;
	public int SN;
	public byte[] Buff;
	public int Position;
	private int writeindex = 0;

	// / <summary>
	// / 要发送出去的字节流【在这里定义是为了更好的销毁】
	// / </summary>
	private byte[] sendBUFF;

	public short Size() {
		if (Buff == null)
			return (short) 0;
		return (short) writeindex;
	}

	public void Size(int value) {
		writeindex = value;
	}

	public TCPMessage() {
	}

	public TCPMessage(int type) {
		// 初始化SN和EC
		SN = 0;
		Buff = new byte[128];
		Type = type;
		Position = 0;
	}

	public byte[] GetByte() {
		if (sendBUFF == null) {
			sendBUFF = new byte[this.Size() + 10];
			System.arraycopy(new byte[] { 123 }, 0, sendBUFF, 0, 1);
			System.arraycopy(DigitalUtils.int2Byte(this.Type), 0, sendBUFF, 1,
					2);
			System.arraycopy(DigitalUtils.int2Byte(this.SN), 0, sendBUFF, 3, 4);
			System.arraycopy(DigitalUtils.int2Byte(this.Size()), 0, sendBUFF,
					7, 2);
			System.arraycopy(this.Buff, 0, sendBUFF, 9, writeindex);
			System.arraycopy(new byte[] { 125 }, 0, sendBUFF, 9 + writeindex, 1);
		}
		return sendBUFF;
	}

	// / <summary>
	// / 写入指定长度的字符串,不足用0补齐
	// / </summary>
	public void Write(String str, int length) {
		if (str == null)
			str = "";

		byte[] strByte = null;
		byte[] strByte2 = new byte[length];
		try {
			strByte = str.getBytes("UTF-8");
			if (strByte.length <= length)// 防止溢出
				System.arraycopy(strByte, 0, strByte2, 0, strByte.length);
			else
				System.arraycopy(strByte, 0, strByte2, 0, length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.e("TCPMessage", e.getMessage());
		}

		Write(strByte2);
	}

	// / <summary>
	// / 写入bool
	// / </summary>
	// / <param name="num">要写入的byte</param>
	public void Write(Boolean num) {
		Write(num ? (byte) 1 : (byte) 0);
	}

	// / <summary>
	// / 写入数字
	// / </summary>
	// / <param name="num">要写入的数字</param>
	public void Write(short num) {
		byte[] _bytenum = DigitalUtils.short2Byte(num);
		Write(_bytenum);
	}

	// / <summary>
	// / 写入数字
	// / </summary>
	// / <param name="num">要写入的数字</param>
	public void Write(int num) {
		byte[] _bytenum = DigitalUtils.int2Byte(num);
		Write(_bytenum);
	}

	// / <summary>
	// / 写入数字
	// / </summary>
	// / <param name="num">要写入的数字</param>
	public void Write(long num) {
		byte[] _bytenum = DigitalUtils.longToByte(num);
		Write(_bytenum);
	}

	// / <summary>
	// / 写入数字
	// / </summary>
	// / <param name="num">要写入的数字</param>
	public void Write(double num) {
		byte[] _bytenum = DigitalUtils.double2byte(num);
		Write(_bytenum);
	}

	// / <summary>
	// / 写入数字
	// / </summary>
	// / <param name="num">要写入的数字</param>
	public void Write(float num) {
		byte[] _bytenum = DigitalUtils.float2byte(num);
		Write(_bytenum);
	}

	// / <summary>
	// / 写入字节
	// / </summary>
	// / <param name="byt">要写入的字节</param>
	public void Write(byte byt) {
		Write(new byte[] { byt });
	}

	// / <summary>
	// / 写入字节流
	// / </summary>
	// / <param name="_bytenum">要写入的字节流</param>
	public void Write(byte[] _bytenum) {
		while (_bytenum.length + writeindex > Buff.length)
			addbuffsize();
		System.arraycopy(_bytenum, 0, Buff, writeindex, _bytenum.length);

		writeindex += _bytenum.length;
		sendBUFF = null;
	}

	private void addbuffsize() {
		int l = Buff.length;
		if (l == 0)
			l = 64;
		byte[] _byte = new byte[l * 2];
		System.arraycopy(Buff, 0, _byte, 0, Buff.length);
		Buff = _byte;
	}

	/***************************** 通用读取 ********************************/
	public Boolean ReadBool() {
		Position++;
		return Buff[Position - 1] == 1 ? true : false;
	}

	public byte ReadByte() {
		Position++;
		return Buff[Position - 1];
	}

	public byte[] ReadBytes(int startindex, int lengnt) {
		byte[] newbyte = new byte[lengnt];
		System.arraycopy(Buff, startindex, newbyte, 0, lengnt);
		return newbyte;
	}

	// / <summary>
	// / 获得字节数组
	// / </summary>
	// / <param name="lenght">字节数组长度</param>
	// / <returns></returns>
	public byte[] ReadBytes(int lenght) {
		byte[] newbyte = new byte[lenght];
		System.arraycopy(Buff, Position, newbyte, 0, lenght);
		Position += lenght;
		return newbyte;
	}

	public Double ReadDouble() {
		Position += 8;
		return DigitalUtils.byte2double(Buff, Position - 8);
	}

	public float ReadFloat() {
		Position += 4;
		return DigitalUtils.byte2float(Buff, Position - 4);
	}

	public int ReadInt() {
		Position += 4;
		return DigitalUtils.byte2Int(Buff, Position - 4);
	}

	public long ReadLong() {
		Position += 8;
		return DigitalUtils.byte2long(Buff, Position - 8);
	}

	public short ReadShort() {
		Position += 2;
		return DigitalUtils.byte2Short(Buff, Position - 2);
	}

	public String ReadString(int lenght) {
		Position += lenght;
		byte[] buff = new byte[lenght];
		System.arraycopy(Buff, Position - lenght, buff, 0, lenght);
		return DigitalUtils.byte2String(buff);
	}

	// public String ReadHexString(int lenght) {
	// int i = Position;
	// Position += lenght;
	// StringBuilder sb = new StringBuilder();
	// for (; i < Position; i++) {
	// sb.Append(Buff[i].ToString("X2"));
	// }
	// return sb.ToString();
	// }
}
