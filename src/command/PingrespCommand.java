package command;

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
		remainingLength = 0;
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[]{typeFlag, remainingLength};
		
		return mergeBytes;
	}
	
	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
