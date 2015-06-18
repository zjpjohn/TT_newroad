package com.newroad.util.iohandler;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TypeConvertor {
	
	private TypeConvertor(){
	}

	/** */
	/**
	 * 文件转化为字节数组
	 */
	public static byte[] file2byte(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			for (int n; (n = stream.read(b)) != -1;) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	/** */
	/**
	 * 把字节数组保存为一个文件
	 */
	public static File byte2file(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/** */
	/**
	 * 从字节数组获取对象
	 * 
	 * @Author Sean.guo
	 * @EditTime 2007-8-13 上午11:46:34
	 */
	public static Object byte2object(byte[] objBytes) throws Exception {
		if (objBytes == null || objBytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		return oi.readObject();
	}

	public static Document String2Document(String xmlStr) {
		StringReader sr = new StringReader(xmlStr);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.parse(is);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/** */
	/**
	 * 从对象获取一个字节数组
	 * 
	 * @EditTime 2007-8-13 上午11:46:56
	 */
	public static byte[] object2byte(Object fileByte) throws Exception {
		if (fileByte == null) {
			return null;
		}
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo = new ObjectOutputStream(bo);
		oo.writeObject(fileByte);
		return bo.toByteArray();
	}

	public static byte[] inputStream2byte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte[] imgdata = bytestream.toByteArray();
		is.close();
		bytestream.close();
		return imgdata;
	}

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		String imgdata = bytestream.toString();
		is.close();
		bytestream.close();
		return imgdata;
	}

	public static File inputStream2File(InputStream input, String targetFile)
			throws IOException {
		BufferedOutputStream stream = null;
		FileOutputStream fstream = null;
		FileDescriptor fd = null;
		File file = new File(targetFile);
		try {
			file.createNewFile();
			fstream = new FileOutputStream(file);
			fd = fstream.getFD();

			stream = new BufferedOutputStream(fstream);
			int ch;
			while ((ch = input.read()) != -1) {
				stream.write(ch);
				stream.flush();
			}
			// Force write file to disk
			fd.sync();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (stream != null) {
					stream.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return file;
	}

	public static File inputStreamNIO2File(InputStream input, String targetFile)
			throws IOException {
		File file = new File(targetFile);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		ReadableByteChannel readableChannel = Channels.newChannel(input);
		FileChannel foutChannel = raf.getChannel();

		ByteBuffer byteBuffer = ByteBuffer.allocate(4 * 1024);
		while (readableChannel.read(byteBuffer) != -1) {
			byteBuffer.flip();
			while (byteBuffer.hasRemaining()) {
				foutChannel.write(byteBuffer);
				foutChannel.force(true);
			}
			byteBuffer.clear();
		}
		foutChannel.close();
		readableChannel.close();
		raf.close();
		return file;
	}

	/**
	 * byte to int
	 * 
	 * @param b
	 *            待转换的字节数组
	 * @param offset
	 *            偏移量，字节数组中开始转换的位置
	 * @return
	 */
	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8
				| (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	/**
	 * int to byte
	 * 
	 * @param n待转换的整形变量
	 * @param buf
	 *            转换后生成的字节数组
	 * @param offset
	 *            偏移量，字节数组中开始存放的位置
	 */
	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	/**
	 * @returntype void
	 * @param n
	 *            待转换的short变量
	 * @param buf
	 *            转换后存放的byte数组
	 * @param offset偏移量
	 *            ，字节数组中开始存放的位置
	 */
	public static void short2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	/**
	 * 
	 * @param buf
	 * @return
	 */
	public static String byte2Hex(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (byte b : buf) {
			if (b == 0) {
				sb.append("00");
			} else if (b == -1) {
				sb.append("FF");
			} else {
				String str = Integer.toHexString(b).toUpperCase();
				// sb.append(a);
				if (str.length() == 8) {
					str = str.substring(6, 8);
				} else if (str.length() < 2) {
					str = "0" + str;
				}
				sb.append(str);

			}
			sb.append(" ");
		}
		sb.append("}");
		return sb.toString();
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/**
	 * convert signed one byte into a hexadecimal digit
	 * 
	 * @param b
	 *            byte
	 * @return convert result
	 */
	public static String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	/**
	 * convert signed 4 bytes into a 32-bit integer
	 * 
	 * @param buf
	 *            bytes buffer
	 * @param pos
	 *            beginning <code>byte</code>> for converting
	 * @return convert result
	 */
	public static long unsigned4BytesToInt(byte[] buf, int pos) {
		int firstByte = 0;
		int secondByte = 0;
		int thirdByte = 0;
		int fourthByte = 0;
		int index = pos;
		firstByte = (0x000000FF & ((int) buf[index]));
		secondByte = (0x000000FF & ((int) buf[index + 1]));
		thirdByte = (0x000000FF & ((int) buf[index + 2]));
		fourthByte = (0x000000FF & ((int) buf[index + 3]));
		return ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
	}

	public static long bytes2long(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 8; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static byte[] long2bytes(long num) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;
	}

	public static long getLong(byte[] bb, int index) {
		return ((((long) bb[index + 0] & 0xff) << 56)
				| (((long) bb[index + 1] & 0xff) << 48)
				| (((long) bb[index + 2] & 0xff) << 40)
				| (((long) bb[index + 3] & 0xff) << 32)
				| (((long) bb[index + 4] & 0xff) << 24)
				| (((long) bb[index + 5] & 0xff) << 16)
				| (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
	}

	public static void putLong(byte[] bb, long x, int index) {
		bb[index + 0] = (byte) (x >> 56);
		bb[index + 1] = (byte) (x >> 48);
		bb[index + 2] = (byte) (x >> 40);
		bb[index + 3] = (byte) (x >> 32);
		bb[index + 4] = (byte) (x >> 24);
		bb[index + 5] = (byte) (x >> 16);
		bb[index + 6] = (byte) (x >> 8);
		bb[index + 7] = (byte) (x >> 0);
	}

	public static void putShort(byte b[], short s, int index) {
		b[index] = (byte) (s >> 8);
		b[index + 1] = (byte) (s >> 0);
	}

	public static short getShort(byte[] b, int index) {
		return (short) (((b[index] << 8) | b[index + 1] & 0xff));
	}
}
