package util;

public class BoolUtils {
	public static byte getQoS(boolean[] qos) {
		byte _qos = 0;
		if(!qos[0] && !qos[1]) {
			_qos = 0;
		}
		else if(qos[0] && !qos[1]) {
			_qos = 1;
		} else if(!qos[0] && qos[1]) {
			_qos = 2;
		} else {
			_qos = 0;
		}
		return _qos;
	}
	
	public static boolean[] getBoolQoS(byte qos) {
		boolean[] _qos = new boolean[2];
		
		if(qos == 0) {
			_qos[0] = false;
			_qos[1] = false;
		} else if(qos == 1) {
			_qos[0] = true;
			_qos[1] = false;
		} else if(qos == 2) {
			_qos[0] = false;
			_qos[1] = true;
		} else {
			_qos[0] = true;
			_qos[1] = true;
		}
		
		return _qos;
	}
	
	/**
	 * convert boolean array to byte.
	 * for example, booleans = {false, false, false, false, false, false, false, false} -> byte = 0
	 * @param bools boolean array
	 * @return byte
	 */
	public static byte booleansToByte(boolean[] bools) {
		byte b = (byte)((bools[0]?1<<7:0) + (bools[1]?1<<6:0) + (bools[2]?1<<5:0) + 
                (bools[3]?1<<4:0) + (bools[4]?1<<3:0) + (bools[5]?1<<2:0) + 
                (bools[6]?1<<1:0) + (bools[7]?1:0));

		return b;
	}
	
	/**
	 * convert byte to booleans.
	 * for example, byte = 0 -> booleans = {false, false, false, false, false, false, false, false}
	 * @param num byte
	 * @return booleans
	 */
	public static boolean[] byteToBooleans(byte num) {
	    boolean[] booleans = new boolean[8];
	    booleans[0] = ((num & 0x01) != 0);
	    booleans[1] = ((num & 0x02) != 0);
	    booleans[2] = ((num & 0x04) != 0);
	    booleans[3] = ((num & 0x08) != 0);

	    booleans[4] = ((num & 0x16) != 0);
	    booleans[5] = ((num & 0x32) != 0);
	    booleans[6] = ((num & 0x64) != 0);
	    booleans[7] = ((num & 0x128) != 0);
	    
	    return booleans;
	}
}
