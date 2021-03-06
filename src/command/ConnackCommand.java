package command;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
/**
 * CONNACK COMMAND
 * @author JUNE-HOME
 *
 */
public class ConnackCommand extends Command {
 
	// Variable Header (Default)
	byte acknowledgeFlags = 0;
	byte returnCode = 0;
	
	
	public void init() {
		type = PacketType.TYPE_CONNACK;
		flag = PacketFlag.FLAG_CONNACK;
		remainingLength = new byte[] { 2 };
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + ByteUtils.decodeRL(remainingLength)]; 
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		// Fixed
		buffer.put(typeFlag).put(remainingLength).put(acknowledgeFlags).put(returnCode);

		return buffer.array();
	}
	
	@Override
	public void print() {
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Acknowledge Flag : " + acknowledgeFlags);
		System.out.println("Return Code : " + returnCode);
	}
	
	/**
	 * bit 7 ~ 1 : Reserved.
	 * bit 0 : Session Present Flag.
	 * 
	 * @param acknowledgeFlags 
	 */
	public void setAcknowledgeFlags(byte acknowledgeFlags) {
		this.acknowledgeFlags = acknowledgeFlags;
	}

	/**
	 * Value 0x00 : Connection Accepted.
	 * Value 0x01 : Connection Refused, unacceptable protocol version.
	 * Value 0x02 : Connection Refused, identifier rejected.
	 * Value 0x03 : Connection Refused, server unavailable.
	 * Value 0x04 : Connection Refused, bad user name or password.
	 * Value 0x05 : Connection Refused, not authorized.
	 * Value 0x06~0xff : Reserved for future use.
	 * 
	 * @param returnCode
	 */
	public void setReturnCode(byte returnCode) {
		this.returnCode = returnCode;
	}	
	
	
}
