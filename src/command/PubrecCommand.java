package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
/**
 * PUBREC COMMAND
 * @author JUNE-HOME
 *
 */
public class PubrecCommand extends Command {

	// Variable Header (Default)	
	byte msbPakcetIdentifier = 0;
	byte lsbPacketIdentifier = 0;
	
	public void init() {
		type = PacketType.TYPE_PUBREC;
		flag = PacketFlag.FLAG_PUBREC;
		remainingLength = new byte[]{2};
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + remainingLength[0]]; // TypeFlag + Remaining Length (1 byte) + MSB LSB Identifier(2 byte)
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		buffer.put(typeFlag).put(remainingLength).put(msbPakcetIdentifier).put(lsbPacketIdentifier);
		
		return buffer.array();
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
