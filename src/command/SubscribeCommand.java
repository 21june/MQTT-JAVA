package command;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import constants.PacketFlag;
import constants.PacketType;
import util.BoolUtils;
import util.ByteUtils;
import util.ParseUtils;
import util.StringUtils;

/**
 * SUBSCRIBE COMMAND
 * 
 * @author JUNE-HOME
 *
 */
public class SubscribeCommand extends Command {
	// Variables
	byte msbLengthforPacketID = 0;
	byte lsbLengthforPacketID = 0;

	// Payload
	private ArrayList<Byte> msbLengthforTopic = new ArrayList<Byte>();
	private ArrayList<Byte> lsbLengthforTopic = new ArrayList<Byte>();
	private ArrayList<Byte[]> topicFilter = new ArrayList<Byte[]>();
	private ArrayList<Byte> qos = new ArrayList<Byte>();
	private byte[] payload = {};

	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_SUBSCRIBE;
		flag = PacketFlag.FLAG_SUBSCRIBE;
		remainingLength = new byte[] { 2 };

		msbLengthforTopic.add(new Byte((byte) 0));
		lsbLengthforTopic.add(new Byte((byte) 11));
		topicFilter.add(ByteUtils.toObjects(new byte[] { 't', 'e', 'm', 'p', '/', 'r', 'a', 'n', 'd', 'o', 'm' }));
		qos.add(new Byte((byte) 0));
		msbLengthforTopic.add(new Byte((byte) 0));
		lsbLengthforTopic.add(new Byte((byte) 4));
		topicFilter.add(ByteUtils.toObjects(new byte[] { 'j', 'u', 'n', 'e' }));
		qos.add(new Byte((byte) 1));
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntValueRL());
		byte typeFlag = ByteUtils.fixedHeaderCalc(type, flag);
		boolean[] flags = ParseUtils.getFlags(typeFlag);
		boolean[] qos = { flags[1], flags[2] };
		byte _qos = BoolUtils.getQoS(qos);
		byte[] mergeBytes = new byte[1 + 1 + ByteUtils.decodeRL(remainingLength)];
		ByteBuffer buffer = ByteBuffer.wrap(mergeBytes);

		byte[] payload = getPayload();
		// Fixed
		buffer.put(ByteUtils.fixedHeaderCalc(type, flag));
		buffer.put(remainingLength);

		// Variable
		if (_qos == 1 || _qos == 2) {
			buffer.put(msbLengthforPacketID);
			buffer.put(lsbLengthforPacketID);
		}
		// Payload
		buffer.put(payload);
		return buffer.array();
	}

	public byte[] getPayload() {
		int length = msbLengthforTopic.size() + lsbLengthforTopic.size() + qos.size();
		for (int i = 0; i < topicFilter.size(); i++) {
			length += topicFilter.get(i).length;
		}
		byte[] payload = new byte[length];
		ByteBuffer buffer = ByteBuffer.wrap(payload);
		for (int i = 0; i < topicFilter.size(); i++) {
			Byte[] temp = topicFilter.get(i);
			buffer.put(msbLengthforTopic.get(i)).put(lsbLengthforTopic.get(i)).put(ByteUtils.toPrimitives(topicFilter.get(i))).put(qos.get(i));
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

	public void setQos(ArrayList<Byte> qos) {
		this.qos = qos;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}
	
	public void setCustomPacketID(int ID) {
		byte[] _MSBLSB = ByteUtils.getMsbLsb(ID);
		setMsbLengthforPacketID(_MSBLSB[0]);
		setLsbLengthforPacketID(_MSBLSB[1]);
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

	public ArrayList<Byte> getQos() {
		return qos;
	}

	
	
	public void setCustomTopicFilter(String[] topics, int[] qos) {
		int length = topics.length;
		ArrayList<Byte> _msb = new ArrayList<Byte>();
		ArrayList<Byte> _lsb = new ArrayList<Byte>();
		ArrayList<Byte[]> _topicfilter = new ArrayList<Byte[]>();
		ArrayList<Byte> _qos = new ArrayList<Byte>();
	
		for(int i=0; i<length; i++) {
			byte[] bTopicName = StringUtils.getUTF8BytesFromString(topics[i]);
			byte[] bMSBLSB = ByteUtils.getMsbLsb(topics[i].length());
			_msb.add(bMSBLSB[0]);
			_lsb.add(bMSBLSB[1]);
			_topicfilter.add(ByteUtils.toObjects(bTopicName));
			_qos.add((byte)qos[i]);
		}
		setMsbLengthforTopic(_msb);
		setLsbLengthforTopic(_lsb);
		setTopicFilter(_topicfilter);
		setQos(_qos);
	}

	@Override
	public void print() {	
		System.out.println("");
		System.out.println("Type : " + type);
		System.out.println("Flag : " + flag);
		System.out.println("Packet ID : " + ByteUtils.calcLengthMSBtoLSB(msbLengthforPacketID, lsbLengthforPacketID));
		for (int i = 0; i < topicFilter.size(); i++) {
			System.out.println("[Topic " + i + "] Size: " + ByteUtils.calcLengthMSBtoLSB(msbLengthforTopic.get(i), lsbLengthforTopic.get(i)) + ", Value : " + new String(ByteUtils.toPrimitives(topicFilter.get(i)))
					+ ", QoS : " + qos.get(i));
		}
	}
}
