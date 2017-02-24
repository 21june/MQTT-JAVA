package command;

import java.nio.ByteBuffer;
import java.util.Arrays;

import constants.PacketFlag;
import constants.PacketType;
import util.BoolUtils;
import util.ByteUtils;
import util.ParseUtils;

/**
 * CONNECT COMMAND
 * 
 * @author JUNE-HOME
 *
 */
public class ConnectCommand extends Command {

	// Variable Header
	private byte msbLengthforProtocolName = 0;
	private byte lsbLengthforProtocolName = 4;
	private byte[] protocolName = { 'M', 'Q', 'T', 'T' };
	private byte protocolLevel = 4;
	private byte connectFlag = 0;
	private byte msbKeepAlive = 10;
	private byte lsbKeepAlive = 10;

	// Payload
	private byte msbLengthforIdentifier = 0;
	private byte lsbLengthforIdentifier = 8;
	private byte identifier[] = { 66, 66, 66, 66, 66, 66, 66, 66 };

	private byte msbLengthforWillTopic = 0;
	private byte lsbLengthforWillTopic = 3;
	private byte willTopic[] = {'a', '/', 'b'};

	private byte msbLengthforWillMessage = 0;
	private byte lsbLengthforWillMessage = 3;
	private byte willMessage[] = {'R', 'I', 'P'};

	private byte msbLengthforUserName = 0;
	private byte lsbLengthforUserName = 0;
	private byte userName[] = null;

	private byte msbLengthforPassword = 0;
	private byte lsbLengthforPassword = 0;
	private byte password[] = null;

	private boolean[] flags = new boolean[8];

	public void init() {
		// fixed header
		type = PacketType.TYPE_CONNECT;
		flag = PacketFlag.FLAG_CONNECT;
		flags = ParseUtils.getFlags(connectFlag);
	}

	@Override
	public byte[] merge() {
		/**
		 * ConnectFlag Setting
		 */
		setFlagCleanSession(true);
		setFlagWillFlag(true);
		
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntValueRL());
		int arrayLenRL = ByteUtils.getArrayLenRL(getIntValueRL());
		byte[] mergedBytes = new byte[1 + arrayLenRL + getIntValueRL()];

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
		buffer.put(identifier);
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

	/**
	 * Remaining Length
	 * 
	 * get integer RL. using this, convert to byte array RL.
	 * 
	 * @return Integer Remaining Length.
	 */
	public int getIntValueRL() {
		int temp = 0;
		// Variable Length
		temp = 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforProtocolName, lsbLengthforProtocolName); // Protocol Name MSB, LSB(2 bytes) + protocol name (? bytes)

		temp += 1; // Protocol Level (1 byte)
		temp += 1; // Connect Flags (1 byte)
		temp += 2; // Keep Alive MSB, LSB (2 bytes)

		// Payload Length
		temp += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforIdentifier, lsbLengthforIdentifier); // Client Identifier MSB, LSB(2 bytes) + identifier (? bytes)

		if (flags[2]) { // if Will Flag set 1
			temp += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillTopic, lsbLengthforWillTopic);
			temp += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillMessage, lsbLengthforWillMessage);
		}

		if (flags[7]) { // if User Name Flag set 1
			temp += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforUserName, lsbLengthforUserName);
		}

		if (flags[6]) { // if Password Flag set 1
			temp += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforPassword, lsbLengthforPassword);
		}

		return temp;
	}

	/**
	 * Getter & Setter Functions
	 */

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

	public ConnectCommand setIdentifier(byte[] identifier) {
		this.identifier = identifier;
		return this;
	}

	public byte getMsbLengthforProtocolName() {
		return msbLengthforProtocolName;
	}

	public byte getLsbLengthforProtocolName() {
		return lsbLengthforProtocolName;
	}

	public byte[] getProtocolName() {
		return protocolName;
	}

	public byte getProtocolLevel() {
		return protocolLevel;
	}

	public byte getConnectFlag() {
		return connectFlag;
	}

	public byte getMsbKeepAlive() {
		return msbKeepAlive;
	}

	public byte getLsbKeepAlive() {
		return lsbKeepAlive;
	}

	public byte getMsbLengthforIdentifier() {
		return msbLengthforIdentifier;
	}

	public byte getLsbLengthforIdentifier() {
		return lsbLengthforIdentifier;
	}

	public byte[] getIdentifier() {
		return identifier;
	}

	public byte getMsbLengthforWillTopic() {
		return msbLengthforWillTopic;
	}

	public byte getLsbLengthforWillTopic() {
		return lsbLengthforWillTopic;
	}

	public byte[] getWillTopic() {
		return willTopic;
	}

	public byte getMsbLengthforWillMessage() {
		return msbLengthforWillMessage;
	}

	public byte getLsbLengthforWillMessage() {
		return lsbLengthforWillMessage;
	}

	public byte[] getWillMessage() {
		return willMessage;
	}

	public byte getMsbLengthforUserName() {
		return msbLengthforUserName;
	}

	public byte getLsbLengthforUserName() {
		return lsbLengthforUserName;
	}

	public byte[] getUserName() {
		return userName;
	}

	public byte getMsbLengthforPassword() {
		return msbLengthforPassword;
	}

	public byte getLsbLengthforPassword() {
		return lsbLengthforPassword;
	}

	public byte[] getPassword() {
		return password;
	}

	public boolean[] getFlags() {
		return flags;
	}

	public void setFlagCleanSession(boolean bool) {
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[1] = bool;
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public void setFlagWillFlag(boolean bool) {
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[2] = bool;
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public void setFlagWillQoS(byte qos) {
		boolean[] temp = BoolUtils.getBoolQoS(qos);
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[3] = temp[0];
		flags[4] = temp[1];
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public void setFlagWillRetain(boolean bool) {
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[5] = bool;
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public void setFlagPassword(boolean bool) {
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[6] = bool;
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public void setFlagUserName(boolean bool) {
		flags = BoolUtils.byteToBooleans(connectFlag);
		flags[7] = bool;
		connectFlag = BoolUtils.booleansToByte(flags);
	}

	public boolean getFlagCleanSession() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		return flags[1];
	}

	public boolean getFlagWill() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		return flags[2];
	}

	public byte getFlagWillQoS() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		boolean[] qos = new boolean[2];
		qos[0] = flags[3];
		qos[1] = flags[4];
		
		byte temp = BoolUtils.getQoS(qos);
		
		return temp;
	}
	
	public boolean getFlagWillRetain() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		return flags[5];
	}

	public boolean getFlagPassword() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		return flags[6];
	}

	public boolean getFlagUserName() {
		flags = BoolUtils.byteToBooleans(connectFlag);
		return flags[7];
	}

	@Override
	public void print() {
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Protocol Name : " + new String(protocolName));
		System.out.println("Protocol Level : " + protocolLevel);
		System.out.println("Connect Flags : " + Arrays.toString(ParseUtils.getFlags(connectFlag)));
		System.out.println("Keep Alive : " + ByteUtils.calcLengthMSBtoLSB(msbKeepAlive, lsbKeepAlive));
		System.out.println("Client Identifier Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforIdentifier, lsbLengthforIdentifier));
		System.out.println("Client Identifier : " + new String(identifier));

		System.out.println("Will Topic Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillTopic, lsbLengthforWillTopic));
		if (willTopic == null)
			System.out.println("Will Topic : [Null]");
		else
			System.out.println("Will Topic : " + new String(willTopic));

		System.out.println("Will Message Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforWillMessage, lsbLengthforWillMessage));
		if (willMessage == null)
			System.out.println("Will Message : [Null]");
		else
			System.out.println("Will Message : " + new String(willMessage));

		System.out.println("Will Retain : " + getFlagWillRetain());
		
		System.out.println("User Name Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforUserName, lsbLengthforUserName));
		if (userName == null)
			System.out.println("User Name : [Null]");
		else
			System.out.println("User Name : " + new String(userName));

		System.out.println("Password Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforPassword, lsbLengthforPassword));
		if (password == null)
			System.out.println("Password : [Null]");
		else
			System.out.println("Password : " + new String(password));
	}

}