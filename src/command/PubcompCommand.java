package command;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * PUBCOMP COMMAND
 * @author JUNE-HOME
 *
 */
public class PubcompCommand extends Command {

	// Variable Header (Default)
	byte msbIdentifier = 0;
	byte lsbIdentifier = 0;

	public void init() {
		type = PacketType.TYPE_PUBCOMP;
		flag = PacketFlag.FLAG_PUBCOMP;
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
