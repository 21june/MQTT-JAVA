package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * DISCONNECT COMMAND
 * @author JUNE-HOME
 *
 */
public class DisconnectCommand extends Command {

	public void init() {
		type = PacketType.TYPE_DISCONNECT;
		flag = PacketFlag.FLAG_DISCONNECT;
		remainingLength = new byte[] {0};
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + remainingLength[0]];
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);
		buffer.put(typeFlag).put(remainingLength);
		return buffer.array();
	}
	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
