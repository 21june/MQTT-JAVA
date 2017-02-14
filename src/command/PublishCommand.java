package command;

import java.nio.ByteBuffer;
import constants.PacketType;
import util.ByteUtils;

/**
 * PUBLISH COMMAND
 * @author JUNE-HOME
 *
 */
public class PublishCommand extends Command {

	// Fixed Header
	boolean dupFlag = false;
	boolean[] qos = {false, false};
	boolean retain = false;
	
	// Variable Header
	private byte msbLengthforTopic = 0;
	private byte lsbLengthforTopic = 3;
	private byte[] topicName = "a/b".getBytes();
	private byte msbLengthforPacketID;
	private byte lsbLengthforPacketID;
	
	// Payload
	private byte[] payload = "Hello~".getBytes();
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_PUBLISH;
		flag = 0;
		remainingLength = new byte[]{0}; // ¼öÁ¤ ¿ä¸Á
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntValueRL());
		int arrayLenRL = ByteUtils.getArrayLenRL(getIntValueRL());
		byte[] mergedBytes = new byte[1 + arrayLenRL + getIntValueRL()];
		System.out.println("Merged Bytes Length : " + mergedBytes.length);
		
		ByteBuffer buffer = ByteBuffer.wrap(mergedBytes);
		// Fixed Header
		buffer.put(ByteUtils.fixedHeaderCalc(type, flag));
		buffer.put(remainingLength);
		
		// Variable Header
		buffer.put(msbLengthforTopic);
		buffer.put(lsbLengthforTopic);
		buffer.put(topicName);
		
		buffer.put(msbLengthforPacketID);
		buffer.put(lsbLengthforPacketID);
		
		// Payload
		buffer.put(payload);
		
		return buffer.array();
	}
	
	/**
	 * get integer RL. using this, convert to byte array RL.
	 * 
	 * @return Integer Remaining Length.
	 */
	public int getIntValueRL() {
		int temp = 0;
		
		temp = 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforTopic, lsbLengthforTopic); // Topic Name MSB, LSB (2 bytes) + Value (??? bytes)
		temp += 2; // Packet Identifier MSB, LSB (2 bytes)
		temp += payload.length; // Payload Length (??? bytes)
		
		return temp;
	}
	
	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

	public void setMsbLengthforTopic(byte msbLengthforTopic) {
		this.msbLengthforTopic = msbLengthforTopic;
	}

	public void setLsbLengthforTopic(byte lsbLengthforTopic) {
		this.lsbLengthforTopic = lsbLengthforTopic;
	}

	public void setTopicName(byte[] topicName) {
		this.topicName = topicName;
	}

	public void setMsbLengthforPacketID(byte msbLengthforPacketID) {
		this.msbLengthforPacketID = msbLengthforPacketID;
	}

	public void setLsbLengthforPacketID(byte lsbLengthforPacketID) {
		this.lsbLengthforPacketID = lsbLengthforPacketID;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
	@Override
	public void print() {		
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Topic Name Length : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforTopic, lsbLengthforTopic));
		System.out.println("Topic Name : " + new String(topicName));
		System.out.println("Packet ID  : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforPacketID, lsbLengthforPacketID));
		System.out.println("Payload Length: " + payload.length);
		System.out.println("Payload : " + new String(payload));
	}

}
