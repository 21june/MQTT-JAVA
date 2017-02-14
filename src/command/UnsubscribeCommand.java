package command;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
/**
 * UNSUBSCRIBE COMMAND
 * @author JUNE-HOME
 *
 */
public class UnsubscribeCommand extends Command {

	// Variable Header
	private byte msbLengthforPacketID = 0;
	private byte lsbLengthforPacketID = 0;
	
	// Payload
	private ArrayList<Byte> msbLengthforTopic = new ArrayList<Byte>();
	private ArrayList<Byte> lsbLengthforTopic = new ArrayList<Byte>();
	private ArrayList<Byte[]> topicFilter = new ArrayList<Byte[]>();
	private byte[] payload = {};
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_UNSUBSCRIBE;
		flag = PacketFlag.FLAG_UNSUBSCRIBE;
		remainingLength = new byte[]{2};

		msbLengthforTopic.add(new Byte((byte) 0));
		lsbLengthforTopic.add(new Byte((byte) 1));
		topicFilter.add(ByteUtils.toObjects(new byte[]{65}));
		msbLengthforTopic.add(new Byte((byte) 0));
		lsbLengthforTopic.add(new Byte((byte) 1));
		topicFilter.add(ByteUtils.toObjects(new byte[]{66}));
	}
 
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntValueRL());
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		byte[] mergeBytes = new byte[1 + 1 + ByteUtils.decodeRL(remainingLength)]; 
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);

		byte[] payload = getPayload();
		// Fixed
		buffer.put(typeFlag).put(remainingLength);
		// Variable
		buffer.put(msbLengthforPacketID).put(lsbLengthforPacketID);
		// Payload
		buffer.put(payload);
	
		System.out.println(Arrays.toString(buffer.array()));
		return buffer.array();
	}
	
	public byte[] getPayload() {
		// get Length
		int length = msbLengthforTopic.size() + lsbLengthforTopic.size();
		for(int i=0; i<topicFilter.size(); i++) {
			length += topicFilter.get(i).length;
		}
		byte[] payload = new byte[length];
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		for(int i=0; i<topicFilter.size(); i++) { 
			Byte[] temp = topicFilter.get(i);
			buffer.put(msbLengthforTopic.get(i)).put(lsbLengthforTopic.get(i)).put(ByteUtils.toPrimitives(topicFilter.get(i)));
		}
		
		return buffer.array();
	}

	
	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
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
		temp = 2; // Packet Identifier MSB, LSB (2 bytes)
		temp += getPayload().length;
		return temp;
	}

	/**
	 * Getter Setter
	 */
	
	
	public void setMsbLengthforPacketID(byte msbLengthforPacketID) {
		this.msbLengthforPacketID = msbLengthforPacketID;
	}

	public void setLsbLengthforPacketID(byte lsbLengthforPacketID) {
		this.lsbLengthforPacketID = lsbLengthforPacketID;
	}

	public void setMsbLengthforTopic(ArrayList<Byte> msbLengthforTopic) {
		this.msbLengthforTopic = msbLengthforTopic;
	}

	public void setLsbLengthforTopic(ArrayList<Byte> lsbLengthforTopic) {
		this.lsbLengthforTopic = lsbLengthforTopic;
	}

	public void setTopicFilter(ArrayList<Byte[]> topicFilter) {
		this.topicFilter = topicFilter;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public byte getMsbLengthforPacketID() {
		return msbLengthforPacketID;
	}

	public byte getLsbLengthforPacketID() {
		return lsbLengthforPacketID;
	}

	public ArrayList<Byte> getMsbLengthforTopic() {
		return msbLengthforTopic;
	}

	public ArrayList<Byte> getLsbLengthforTopic() {
		return lsbLengthforTopic;
	}

	public ArrayList<Byte[]> getTopicFilter() {
		return topicFilter;
	}
	
	
	@Override
	public void print() {	
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Packet ID : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforPacketID, lsbLengthforPacketID));
		for (int i = 0; i < topicFilter.size(); i++) {
			System.out.println("[Topic " + i + "] Size: " + ByteUtils.calcLengthMSBtoLSB(msbLengthforTopic.get(i), lsbLengthforTopic.get(i)) + ", Value : " + new String(ByteUtils.toPrimitives(topicFilter.get(i))));
		}
	}
}
