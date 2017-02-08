package util;

import java.io.UnsupportedEncodingException;

public class StringUtils {

	/**
	 * from Big-Endian to Little-Endian
	 * 
	 * @param v Big-Endian Integer
	 * @return Little-Endian Bytes
	 */
	public static byte[] getLittleEndian(int v) {
		byte[] buf = new byte[4];
		buf[3] = (byte) ((v >>> 24) & 0xFF);
		buf[2] = (byte) ((v >>> 16) & 0xFF);
		buf[1] = (byte) ((v >>> 8) & 0xFF);
		buf[0] = (byte) ((v >>> 0) & 0xFF);
		return buf;
	}

	/**
	 * from Little-Endian to Big-Endian
	 * 
	 * @param v Little-Endian Integer
	 * @return Big-Endian Bytes
	 * @throws Exception
	 */
	public static int getBigEndian(byte[] v) throws Exception {
		int[] arr = new int[4];
		for (int i = 0; i < 4; i++) {
			arr[i] = (int) (v[3 - i] & 0xFF);
		}
		return ((arr[0] << 24) + (arr[1] << 16) + (arr[2] << 8) + (arr[3] << 0));
	}

	/**
	 * From String To UTF-8 Byte Array
	 * 
	 * @param original String to be converted to UTF-8 format
	 * @return converted Byte Array
	 */
	public static byte[] getUTF8BytesFromString(String original) {
		byte[] utf8Bytes = {(byte) 0};
		try {
			utf8Bytes = original.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return utf8Bytes;
	}
	
}
