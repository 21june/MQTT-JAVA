package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
/**
 * SUBACK COMMAND
 * @author JUNE-HOME
 *
 */
public class SubackCommand extends Command {

	// Variable Header
	byte msbLengthforPacketID = 0;
	byte lsbLengthforPacketID = 0;
	
	// Payload
	byte returncode[] = {};
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_SUBACK;
		flag = PacketFlag.FLAG_SUBACK;
		remainingLength = new byte[]{2};
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntValueRL());
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + ByteUtils.decodeRL(remainingLength)]; 
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		
		// Fixed
		buffer.put(typeFlag).put(remainingLength);
		
		// Variable
		buffer.put(msbLengthforPacketID).put(lsbLengthforPacketID);

		// Payload
		buffer.put(returncode);
		
		return buffer.array();
	}

	/**
	 * Remaining Length
	 * 
	 * get integer RL. using this, convert to byte array RL.
	 * 
	 * @return Integer Remaining Length.
	 */
	public int getIntValueRL() {
		int temp = 0;
		// Variable Length
		temp = 2; // Packet Identifier MSB, LSB (2 bytes)
		temp += returncode.length;
		return temp;
	}

	/**
	 * Getter Setter
	 */
	
	public void setMsbLengthforPacketID(byte msbLengthforPacketID) {
		this.msbLengthforPacketID = msbLengthforPacketID;
	}

	public void setLsbLengthforPacketID(byte lsbLengthforPacketID) {
		this.lsbLengthforPacketID = lsbLengthforPacketID;
	}

	public void setReturncode(byte[] returncode) {
		this.returncode = returncode;
	}

	@Override
	public void print() {
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Packet ID : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforPacketID, lsbLengthforPacketID));
		for(int i=0; i<returncode.length; i++) {
			System.out.println("[Return Code " + i +"] : " + returncode[i]);
		}
	}
	
}
