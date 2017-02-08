package command;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * PUBACK COMMAND
 * @author JUNE-HOME
 *
 */
public class PubackCommand extends Command {

	// Variable Header (Default)
	byte msbIdentifier = 0;
	byte lsbIdentifier = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_PUBACK;
		flag = PacketFlag.FLAG_PUBACK;
		remainingLength = new byte[]{2};
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = null;		
		return mergeBytes;
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	public void setMsbIdentifier(byte msbIdentifier) {
		this.msbIdentifier = msbIdentifier;
	}

	public void setLsbIdentifier(byte lsbIdentifier) {
		this.lsbIdentifier = lsbIdentifier;
	}

	
}
