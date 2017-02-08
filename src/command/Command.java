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
	
	/**
	 * parsing byte array from client.
	 * @param bytes receiving packet from client.
	 */
	abstract public void parse(byte[] bytes);
}
