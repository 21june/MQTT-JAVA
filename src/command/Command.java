package command;

import constants.PacketFlag;
import constants.PacketType;

public abstract class Command {
	// fixed header
	byte type = PacketType.TYPE_CONNECT;
	byte flag = PacketFlag.FLAG_CONNECT;
	byte[] remainingLength;	

	/**
	 * initializing variables.
	 */
	abstract public void init();


	/**
	 * merging packet bytes to byte array for sending to server.
	 * @return byte array for sending to server.
	 */
	abstract public byte[] merge();
	abstract public void parse(byte[] bytes);
	public void print() {
		
	}

	public void setType(byte type) {
		this.type = type;
	}


	public void setFlag(byte flag) {
		this.flag = flag;
	}


	public void setRemainingLength(byte[] remainingLength) {
		this.remainingLength = remainingLength;
	}


	public byte getType() {
		return type;
	}


	public byte getFlag() {
		return flag;
	}


	public byte[] getRemainingLength() {
		return remainingLength;
	}
	
}
