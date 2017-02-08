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
	
	public static final int lengthRLArray(int X) {
		int result = 0;
		if(X<1) {
			result = 0;
		}
		else if(X<128) {
			result = 1;
		}
		else if(X<16384) {
			result = 2;
		} else if(X<2097151) {
			result = 3;
		} else {
			result = 4;
		}
		
		return result;
	}
	
	/**
	 * Integer RL to Byte Array for sending packet. 
	 * @param X integer value of RL
	 * @return byte array of RL
	 */
	public static final byte[] encodeRL(int X) {
		byte[] output = new byte[lengthRLArray(X)];
		byte temp=0;
		int i=0;
		do {
			temp = (byte) (X % 128);
			X = X / 128;
			if(X>0) {
				temp = (byte) (temp | 128);
			}
			output[i++] = temp;
		} while(X>0);
		
		return output;
	}

	/**
	 * byte array RL to Integer RL for parsing packet.
	 * @param encodedBytes byte array of RL
	 * @return integer value of RL
	 */
	public static final int decodeRL(byte[] encodedBytes) {
		int multiplier = 1;
		int value = 0;
		int i=0;
		byte encodedByte = 0;
		 do {
			 encodedByte = encodedBytes[i++];
			 value += (encodedByte & 127) * multiplier;
			 if(multiplier > 128 * 128 * 128)		
				 return 0; // Throw Error Malformed Remaining Length 
			 multiplier *= 128;
		 } while((encodedByte&128) != 0);
		 
		 return value;
	}
}