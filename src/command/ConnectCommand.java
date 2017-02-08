package command;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
import util.PacketUtils;

/**
 * CONNECT COMMAND
 * @author JUNE-HOME
 *
 */
public class ConnectCommand extends Command {

	// Variable Header
	byte msbLengthforProtocolName = 0;
	byte lsbLengthforProtocolName = 4; 
	byte[] protocolName = {'M', 'Q', 'T', 'T'}; 
	byte protocolLevel = 4; 
	byte connectFlag = 0;
	byte msbKeepAlive = 0;
	byte lsbKeepAlive = 10;
	
	// Payload
	byte clientIdentifier = 0;
	byte willTopic = new Byte(null);
	byte willMessage = new Byte(null);
	byte userName = new Byte(null);
	byte msbLengthforPassword = new Byte(null);
	byte lsbLengthforPassword = new Byte(null);
	byte password = new Byte(null);
	
	boolean[] flags = new boolean[8];
	
	public void init() {
		// fixed header
		type = PacketType.TYPE_CONNECT;
		flag = PacketFlag.FLAG_CONNECT;
		remainingLength = 0;
		
		for(int i=0; i<flags.length; i++) {
			flags[i] = (connectFlag & (1 << i)) != 0;
		} 
	}
	  
	public int getRemainingLength() {
		int remainingLength = 0;
		// Variable Length
		remainingLength = 2 + PacketUtils.msbCalculate(msbLengthforProtocolName); // Protocol Name MSB, LSB(2 bytes) + protocol name (? byte)
		remainingLength += 1; // Protocol Level (1 byte)
		remainingLength += 1; // Connect Flags (1 byte)
		remainingLength += 2; // Keep Alive MSB, LSB (2 bytes)
		
		// Payload Length
		if(flags[2]) { // *** 여기까지 진행함
			remainingLength += 1;
		}
		
		
		return remainingLength;
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}
	
	public int getRL() {
		int length = 0;
		
		length = 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforProtocolName, lsbLengthforProtocolName) + 1 + 2;
		
		return length;
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

	public ConnectCommand setClientIdentifier(byte clientIdentifier) {
		this.clientIdentifier = clientIdentifier;
		return this;
	}

	public ConnectCommand setWillTopic(byte willTopic) {
		this.willTopic = willTopic;
		return this;
	}

	public ConnectCommand setWillMessage(byte willMessage) {
		this.willMessage = willMessage;
		return this;
	}

	public ConnectCommand setUserName(byte userName) {
		this.userName = userName;
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

	public ConnectCommand setPassword(byte password) {
		this.password = password;
		return this;
	}
	
	

}