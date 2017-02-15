package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * PINGRESP COMMAND
 * @author JUNE-HOME
 *
 */
public class PingrespCommand extends Command {
	  
	public void init() {
		type = PacketType.TYPE_PINGRESP;
		flag = PacketFlag.FLAG_PINGRESP;
		remainingLength = new byte[]{0};
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
	
}
