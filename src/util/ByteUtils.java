package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtils {
	
	/**
	 * for calculating fixed header.
	 * 
	 * Type 4bits areshifted to 4 bit left.
	 * then Flag 4bits are added.
	 *
	 * ex) Type 0 0 0 1, Flag 0 1 1 1
	 * 
	 * 1) Type Shifting (to left)
	 * 0 0 0 1 0 0 0 0
	 * 2) Adding (Flag bits)
	 * 0 0 0 1 0 1 1 1
	 * 
	 * @param type
	 * @param flag
	 * @return
	 */
	public static final byte fixedHeaderCalc(byte type, byte flag) {
		byte shiftType = (byte) (type << 4);
		byte result = 0;
		result = (byte) (shiftType + flag);
		
		return result;
	}
	
	public static final int calcLengthMSBtoLSB(byte msb, byte lsb) {
		int result = (int) (msb * Math.pow(2, 8));
		result += lsb;
		
		return result;
	}
	
}