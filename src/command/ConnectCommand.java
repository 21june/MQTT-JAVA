package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

/**
 * CONNECT COMMAND
 * 
 * @author JUNE-HOME
 *
 */
public class ConnectCommand extends Command {

	// Variable Header
	byte msbLengthforProtocolName = 0;
	byte lsbLengthforProtocolName = 4;
	byte[] protocolName = { 'M', 'Q', 'T', 'T' };
	byte protocolLevel = 4;
	byte connectFlag = 0;
	byte msbKeepAlive = 0;
	byte lsbKeepAlive = 10;

	// Payload
	byte msbLengthforIdentifier = 0;
	byte lsbLengthforIdentifier = 0;

	byte msbLengthforWillTopic = 0;
	byte lsbLengthforWillTopic = 0;
	byte willTopic[] = null;

	byte msbLengthforWillMessage = 0;
	byte lsbLengthforWillMessage = 0;
	byte willMessage[] = null;

	byte msbLengthforUserName = 0;
	byte lsbLengthforUserName = 0;
	byte userName[] = null;

	byte msbLengthforPassword = 0;
	byte lsbLengthforPassword = 0;
	byte password[] = null;

	boolean[] flags = new boolean[8];

	public void init() {
		// fixed header
		type = PacketType.TYPE_CONNECT;
		flag = PacketFlag.FLAG_CONNECT;

		for (int i = 0; i < flags.length; i++) {
			flags[i] = (connectFlag & (1 << i)) != 0;
		}
	}

	/**
	 * get integer RL. using this, convert to byte array RL.
	 * 
	 * @return Integer Remaining Length.
	 */
	public int getIntRL() {
		int remainingLength = 0;
		// Variable Length
		remainingLength = 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforProtocolName, 
				lsbLengthforProtocolName); // Protocol Name MSB, LSB(2 bytes) + protocol name (? bytes)
		
		remainingLength += 1; // Protocol Level (1 byte)
		remainingLength += 1; // Connect Flags (1 byte)
		remainingLength += 2; // Keep Alive MSB, LSB (2 bytes)

		// Payload Length
		remainingLength += 2; // Client Identifier MSB, LSB(2 bytes)
		if (flags[2]) { // if Will Flag set 1
			remainingLength += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillTopic, lsbLengthforWillTopic);
			remainingLength += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillMessage, lsbLengthforWillMessage);
		}

		if (flags[7]) { // if User Name Flag set 1
			remainingLength += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforUserName, lsbLengthforUserName);
		}

		if (flags[6]) { // if Password Flag set 1
			remainingLength += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforPassword, lsbLengthforPassword);
		}

		return remainingLength;
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntRL());
		int lengthRL = ByteUtils.lengthRLArray(getIntRL());
		byte[] mergedBytes = new byte[1 + lengthRL + getIntRL()];

		for (int i = 0; i < flags.length; i++) {
			System.out.print(flags[i] + " ");
		}
		System.out.println("");

		ByteBuffer buffer = ByteBuffer.wrap(mergedBytes);
		// Fixed Header
		buffer.put(ByteUtils.fixedHeaderCalc(type, flag));
		buffer.put(remainingLength);

		// Variable Header
		buffer.put(msbLengthforProtocolName);
		buffer.put(lsbLengthforProtocolName);
		buffer.put(protocolName);
		buffer.put(protocolLevel);
		buffer.put(connectFlag);
		buffer.put(msbKeepAlive);
		buffer.put(lsbKeepAlive);

		// Payload
		buffer.put(msbLengthforIdentifier);
		buffer.put(lsbLengthforIdentifier);
		if (flags[2]) {
			buffer.put(msbLengthforWillTopic);
			buffer.put(lsbLengthforWillTopic);
			buffer.put(willTopic);
			buffer.put(msbLengthforWillMessage);
			buffer.put(lsbLengthforWillMessage);
			buffer.put(willMessage);
		}
		if (flags[7]) {
			buffer.put(msbLengthforUserName);
			buffer.put(lsbLengthforUserName);
			buffer.put(userName);
		}
		if (flags[6]) {
			buffer.put(msbLengthforPassword);
			buffer.put(lsbLengthforPassword);
			buffer.put(password);
		}
		return buffer.array();
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub

	}

	public ConnectCommand setMsbLengthforProtocolName(byte msbLengthforProtocolName) {
		this.msbLengthforProtocolName = msbLengthforProtocolName;
		return this;
	}

	public ConnectCommand setLsbLengthforProtocolName(byte lsbLengthforProtocolName) {
		this.lsbLengthforProtocolName = lsbLengthforProtocolName;
		return this;
	}

	public ConnectCommand setProtocolName(byte[] protocolName) {
		this.protocolName = protocolName;
		return this;
	}

	public ConnectCommand setProtocolLevel(byte protocolLevel) {
		this.protocolLevel = protocolLevel;
		return this;
	}

	public ConnectCommand setConnectFlag(byte connectFlag) {
		this.connectFlag = connectFlag;
		return this;
	}

	public ConnectCommand setMsbKeepAlive(byte msbKeepAlive) {
		this.msbKeepAlive = msbKeepAlive;
		return this;
	}

	public ConnectCommand setLsbKeepAlive(byte lsbKeepAlive) {
		this.lsbKeepAlive = lsbKeepAlive;
		return this;
	}

	public ConnectCommand setWillTopic(byte[] willTopic) {
		this.willTopic = willTopic;
		return this;
	}

	public ConnectCommand setWillMessage(byte[] willMessage) {
		this.willMessage = willMessage;
		return this;
	}

	public ConnectCommand setUserName(byte[] userName) {
		this.userName = userName;
		return this;
	}

	public ConnectCommand setPassword(byte[] password) {
		this.password = password;
		return this;
	}

	public ConnectCommand setMsbLengthforIdentifier(byte msbLengthforIdentifier) {
		this.msbLengthforIdentifier = msbLengthforIdentifier;
		return this;
	}

	public ConnectCommand setLsbLengthforIdentifier(byte lsbLengthforIdentifier) {
		this.lsbLengthforIdentifier = lsbLengthforIdentifier;
		return this;
	}

	public ConnectCommand setMsbLengthforWillTopic(byte msbLengthforWillTopic) {
		this.msbLengthforWillTopic = msbLengthforWillTopic;
		return this;
	}

	public ConnectCommand setLsbLengthforWillTopic(byte lsbLengthforWillTopic) {
		this.lsbLengthforWillTopic = lsbLengthforWillTopic;
		return this;
	}

	public ConnectCommand setMsbLengthforWillMessage(byte msbLengthforWillMessage) {
		this.msbLengthforWillMessage = msbLengthforWillMessage;
		return this;
	}

	public ConnectCommand setLsbLengthforWillMessage(byte lsbLengthforWillMessage) {
		this.lsbLengthforWillMessage = lsbLengthforWillMessage;
		return this;
	}

	public ConnectCommand setMsbLengthforUserName(byte msbLengthforUserName) {
		this.msbLengthforUserName = msbLengthforUserName;
		return this;
	}

	public ConnectCommand setLsbLengthforUserName(byte lsbLengthforUserName) {
		this.lsbLengthforUserName = lsbLengthforUserName;
		return this;
	}

	public ConnectCommand setMsbLengthforPassword(byte msbLengthforPassword) {
		this.msbLengthforPassword = msbLengthforPassword;
		return this;
	}

	public ConnectCommand setLsbLengthforPassword(byte lsbLengthforPassword) {
		this.lsbLengthforPassword = lsbLengthforPassword;
		return this;
	}

	public ConnectCommand setFlags(boolean[] flags) {
		this.flags = flags;
		return this;
	}

}