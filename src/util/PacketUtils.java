package util;

public class PacketUtils {

	public static int msbCalculate(byte msb) {
		int calc = msb * ((int) Math.pow(2, 8));
		return calc;
	}
	
}
