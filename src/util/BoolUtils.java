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
}
