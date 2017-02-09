package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * PUBCOMP COMMAND
 * 
 * @author JUNE-HOME
 *
 */
public class PubcompCommand extends Command {

	// Variable Header (Default)
	byte msbPacketIdentifier = 0;
	byte lsbPacketIdentifier = 0;

	public void init() {
		type = PacketType.TYPE_PUBCOMP;
		flag = PacketFlag.FLAG_PUBCOMP;
		remainingLength = new byte[] { 2 };
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + remainingLength[0]];
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		buffer.put(typeFlag).put(remainingLength).put(msbPacketIdentifier).put(lsbPacketIdentifier);
		return buffer.array();
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub

	}
}
