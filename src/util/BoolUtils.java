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
}
