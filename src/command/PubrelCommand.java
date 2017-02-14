package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * PUBREL COMMAND
 * @author JUNE-HOME
 *
 */
public class PubrelCommand extends Command {
	// Variable Header (Default)
	byte msbPacketIdentifier = 0;
	byte lsbPacketIdentifier = 0;
	
	
	public void init() {
		type = PacketType.TYPE_PUBREL;
		flag = PacketFlag.FLAG_PUBREL;
		remainingLength = new byte[]{2};
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + remainingLength[0]]; // TypeFlag + Remaining Length (1 byte) + MSB LSB Identifier(2 byte)
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		buffer.put(typeFlag).put(remainingLength).put(msbPacketIdentifier).put(lsbPacketIdentifier);
		
		return buffer.array();
	}
	
	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Getter Setter
	 */
	
	public void setMsbPacketIdentifier(byte msbPacketIdentifier) {
		this.msbPacketIdentifier = msbPacketIdentifier;
	}

	public void setLsbPacketIdentifier(byte lsbPacketIdentifier) {
		this.lsbPacketIdentifier = lsbPacketIdentifier;
	}
	
	
}
