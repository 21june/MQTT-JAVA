package command;

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
	byte msbIdentifier = 0;
	byte lsbIdentifier = 0;
	
	
	public void init() {
		type = PacketType.TYPE_PUBREL;
		flag = PacketFlag.FLAG_PUBREL;
		remainingLength = 2;
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[]{typeFlag, remainingLength, msbIdentifier, lsbIdentifier};
		
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
