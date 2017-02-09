package command;

import java.nio.ByteBuffer;

import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;
/**
 * SUBSCRIBE COMMAND
 * @author JUNE-HOME
 *
 */
public class SubscribeCommand extends Command {
	// Variables
	byte msbLengthforPacketIdentifier = 0;
	byte lsbLengthforPacketIdentifier = 10;
	
	// Payload
	// "temp/random"
	byte msbLengthforTopic = 0;
	byte lsbLengthforTopic = 11;
	byte[] topicFilter = {'t', 'e', 'm', 'p', '/', 'r', 'a', 'n', 'd', 'o', 'm'};
	byte qos = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_SUBSCRIBE;
		flag = PacketFlag.FLAG_SUBSCRIBE;
	}

	public int getIntRL() {
		int remainingLength = 0;
		remainingLength = 2; // Packet Identifier MSB, LSB (2 byte)
		remainingLength += 2 + ByteUtils.calcLengthMSBtoLSB(msbLengthforTopic, 
				lsbLengthforTopic); // Topic MSB, LSB (2 byte) + Topic Filter(? byte)
		remainingLength += 1; // QoS
				
		return remainingLength;
	}
	
	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		remainingLength = ByteUtils.encodeRL(getIntRL());
		int lengthRL = ByteUtils.lengthRLArray(getIntRL());
		byte[] mergedBytes = new byte[1 + lengthRL + getIntRL()];

		ByteBuffer buffer = ByteBuffer.wrap(mergedBytes);
		// Fixed
		buffer.put(ByteUtils.fixedHeaderCalc(type, flag));
		buffer.put(remainingLength);
		
		// Variable
		buffer.put(msbLengthforPacketIdentifier);
		buffer.put(lsbLengthforPacketIdentifier);
		
		// Payload
		buffer.put(msbLengthforTopic);
		buffer.put(lsbLengthforTopic);
		buffer.put(topicFilter);
		buffer.put(qos);
		
		return buffer.array();
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
