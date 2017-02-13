package util;

import java.util.Arrays;

import command.ConnectCommand;

public class ParseUtils {

	public static int getArrayLenRL(byte[] received) {
		byte[] temp = null;
		int count = 1;
		if(received.length > 5) {
			temp = Arrays.copyOfRange(received, 1, 5);
		} else {
			temp = Arrays.copyOfRange(received, 1, received.length);
		}
	
		for(int i=0; i<temp.length; i++) {
			boolean bit7 = (temp[i] & (1 << 7)) != 0;
			if(bit7)
				count++;
		}
		return count;
	}
	
	public static boolean[] getFlags(byte flag) {
		boolean[] flags = new boolean[8];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = (flag & (1 << i)) != 0;
		}	
		
		return flags;
	}
	
	
}
