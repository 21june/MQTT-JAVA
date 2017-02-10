package util;

import java.util.Arrays;

import command.ConnectCommand;

public class ParseUtils {

	public static void parse(byte[] received) {
		byte[] temp = ByteUtils.convertBytetoBits(received[0]);
		byte type = temp[0];
		byte flag = temp[1];

		switch (type) {
		case 1: // CONNECT
			System.out.print("---{CONNECT MESSAGE}---          ");
			parseConnect(received);
			break;
		case 2: // CONNACK
			System.out.print("---{CONNACK MESSAGE}---          ");
			break;
		case 3: // PUBLISH
			System.out.print("---{PUBLISH MESSAGE}---          ");
			break;
		case 4: // PUBACK
			System.out.print("---{PUBACK MESSAGE}---           ");
			break;
		case 5: // PUBREC
			System.out.print("---{PUBREC MESSAGE}---           ");
			break;
		case 6: // PUBREL
			System.out.print("---{PUBREL MESSAGE}---           ");
			break;
		case 7: // PUBCOMP
			System.out.print("---{PUBCOMP MESSAGE}---          ");
			break;
		case 8: // SUBSCRIBE
			System.out.print("---{SUBSCRIBE MESSAGE}---        ");
			break;
		case 9: // SUBACK
			System.out.print("---{SUBACK MESSAGE}---           ");
			break;
		case 10: // UNSUBSCRIBE
			System.out.print("---{UNSUBSCRIBE MESSAGE}---      ");
			break;
		case 11: // UNSUBACK
			System.out.print("---{UNSUBACK MESSAGE}---         ");
			break;
		case 12: // PINGREQ
			System.out.print("---{PINGREQ MESSAGE}---          ");
			break;
		case 13: // PINGRESP
			System.out.print("---{PINGRESP MESSAGE}---         ");
			break;
		case 14: // DISCONNECT
			System.out.print("---{DISCONNECT MESSAGE}---       ");
			break;
		case 0:
		case 15: // RESERVED
		default:
			System.out.println("�������� �ʴ� Type�Դϴ�.");
			break;
		}
	}
	
	private static ConnectCommand parseConnect(byte[] received) {
		byte[] receive = received;
		byte[] temp;
		int count;
		ConnectCommand result = new ConnectCommand();
		int lengthRL = getIntRL(receive);
		result.setRemainingLength(Arrays.copyOfRange(receive, 1, lengthRL));
		int remainingLength = ByteUtils.decodeRL(result.getRemainingLength());
		// continuing
		return result;
	}

	public static int getIntRL(byte[] received) {
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
		
		System.out.println("----- [Parse] getIntRL : " + count);
		return count;
	}
	
	
}