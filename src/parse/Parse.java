package parse;

import java.util.Arrays;

import command.Command;
import command.ConnackCommand;
import command.ConnectCommand;
import command.PublishCommand;
import util.ByteUtils;
import util.ParseUtils;

/**
 * 
 * parsing byte array.
 * 
 * @param bytes
 *            receiving packet.
 *
 * @author Biocom-June
 *
 */
public class Parse {

	public Command parse(byte[] received) {
		Command parsedCom = null;

		byte[] temp = ByteUtils.convertBytetoBits(received[0]);
		byte type = temp[0];
		byte flag = temp[1];

		switch (type) {
		case 1: // CONNECT
			System.out.print("---{CONNECT MESSAGE}---          ");
			parsedCom = new ConnectCommand();
			parsedCom.init();
			parsedCom.setType(type);
			parsedCom.setFlag(flag);
			parseConnect(parsedCom, received);
			break;
		case 2: // CONNACK
			System.out.print("---{CONNACK MESSAGE}---          ");
			parsedCom = new ConnackCommand();
			parsedCom.init();
			parsedCom.setType(type);
			parsedCom.setFlag(flag);
			parseConnack(parsedCom, received);
			break;
		case 3: // PUBLISH
			System.out.print("---{PUBLISH MESSAGE}---          ");
			parsedCom = new PublishCommand();
			parsedCom.init();
			parsedCom.setType(type);
			parsedCom.setFlag(flag);
			parsePublish(parsedCom, received);
			break;
		case 4: // PUBACK
			System.out.print("---{PUBACK MESSAGE}---           ");
			break;
		case 5: // PUBREC
			System.out.print("---{PUBREC MESSAGE}---           ");
			break;
		case 6: // PUBREL
			System.out.print("---{PUBREL MESSAGE}---           ");
			break;
		case 7: // PUBCOMP
			System.out.print("---{PUBCOMP MESSAGE}---          ");
			break;
		case 8: // SUBSCRIBE
			System.out.print("---{SUBSCRIBE MESSAGE}---        ");
			break;
		case 9: // SUBACK
			System.out.print("---{SUBACK MESSAGE}---           ");
			break;
		case 10: // UNSUBSCRIBE
			System.out.print("---{UNSUBSCRIBE MESSAGE}---      ");
			break;
		case 11: // UNSUBACK
			System.out.print("---{UNSUBACK MESSAGE}---         ");
			break;
		case 12: // PINGREQ
			System.out.print("---{PINGREQ MESSAGE}---          ");
			break;
		case 13: // PINGRESP
			System.out.print("---{PINGRESP MESSAGE}---         ");
			break;
		case 14: // DISCONNECT
			System.out.print("---{DISCONNECT MESSAGE}---       ");
			break;
		case 0:
		case 15: // RESERVED
		default:
			System.out.println("존재하지 않는 Type입니다.");
			break;
		}

		return parsedCom;
	}

	/**
	 * parsing to connect command.
	 * 
	 * @param c
	 *            command variable
	 * @param restMessage
	 *            received message
	 * @return parsed connect message
	 */
	private Command parseConnect(Command c, byte[] received) {
		ConnectCommand temp = (ConnectCommand) c;
		byte[] receivedTemp = received;

		/**
		 * Remaining Length (Value)
		 */	
		// Setting
		temp = (ConnectCommand) updateRemainingLength(temp, receivedTemp);
		// Update receivedTemp (to throw away the parsed header)
		receivedTemp = Arrays.copyOfRange(receivedTemp, (ParseUtils.getArrayLenRL(receivedTemp))+1, receivedTemp.length+1);
		
		/**
		 * Protocol Name (MSB, LSB, Value)
		 */
		int protocolArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
		byte[] protocolName = Arrays.copyOfRange(receivedTemp, 2, protocolArrLen+2);

		// Setting
		temp.setMsbLengthforProtocolName(receivedTemp[0]);
		temp.setLsbLengthforProtocolName(receivedTemp[1]);
		temp.setProtocolName(protocolName);

		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, protocolArrLen+2, receivedTemp.length+1);
		
		/**
		 * Protocol Level (1 byte Value)
		 */
		// Setting
		temp.setProtocolLevel(receivedTemp[0]);
		
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 1, receivedTemp.length+1);
		
		/**
		 * Connect Flag (1 byte Value)
		 */
		byte connectFlag = receivedTemp[0];
		boolean[] flags = ParseUtils.getFlags(connectFlag);
		
		// Setting
		temp.setFlag(connectFlag);
		temp.setFlags(flags);
		
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 1, receivedTemp.length+1);
		
		/**
		 * Keep Alive (MSB, LSB)
		 */
		// Setting
		temp.setMsbKeepAlive(receivedTemp[0]);
		temp.setLsbKeepAlive(receivedTemp[1]);
		
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 2, receivedTemp.length+1);
		
		/**
		 * Client Identifier (MSB, LSB, Value)
		 */
		int clientIdArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
		byte[] clientIdentifier;
		// Setting
		if(clientIdArrLen > 0) {
			clientIdentifier = Arrays.copyOfRange(receivedTemp, 2, clientIdArrLen+2);
			temp.setIdentifier(clientIdentifier);
			temp.setMsbLengthforIdentifier(receivedTemp[0]);
			temp.setLsbLengthforIdentifier(receivedTemp[1]);
		}
		else {
			clientIdentifier = null;
		}
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 2+clientIdArrLen, receivedTemp.length+1);
		
		/**
		 * Will Topic - if will flag is true (MSB, LSB, Value)
		 */
		int willTopicArrLen = 0;
		byte[] willTopic;
		if(flags[2]) {
			willTopicArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
			// Setting
			if(willTopicArrLen > 0) {
				willTopic = Arrays.copyOfRange(receivedTemp, 2, willTopicArrLen+2);
				temp.setWillTopic(willTopic);
				temp.setMsbLengthforWillTopic(receivedTemp[0]);
				temp.setLsbLengthforWillTopic(receivedTemp[1]);
			}
			else {
				willTopic = null;
			}
			// Update
			receivedTemp = Arrays.copyOfRange(receivedTemp, 2+willTopicArrLen, receivedTemp.length+1);
		}
		
		/**
		 * Will Message - if will flag is true (MSB, LSB, Value)
		 */
		int willMessageArrLen = 0;
		byte[] willMessage;
		if(flags[2]) {
			willMessageArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
			// Setting
			if(willMessageArrLen > 0) {
				willMessage = Arrays.copyOfRange(receivedTemp, 2, willMessageArrLen+2);
				temp.setWillMessage(willMessage);			
				temp.setMsbLengthforWillMessage(receivedTemp[0]);
				temp.setLsbLengthforWillMessage(receivedTemp[1]);
			}
			else {
				willMessage = null;
			}
			// Update
			receivedTemp = Arrays.copyOfRange(receivedTemp, 2+willMessageArrLen, receivedTemp.length+1);
		}
	
		/**
		 * User Name - if user name flag is true (MSB, LSB, Value)
		 */
		int userNameArrLen = 0;
		byte[] userName;
		if(flags[7]) {
			willMessageArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
			// Setting
			if(userNameArrLen > 0) {
				userName = Arrays.copyOfRange(receivedTemp, 2, userNameArrLen+2);
				temp.setUserName(userName);		
				temp.setMsbLengthforUserName(receivedTemp[0]);
				temp.setLsbLengthforUserName(receivedTemp[1]);
			}
			else {
				userName = null;
			}
			// Update
			receivedTemp = Arrays.copyOfRange(receivedTemp, 2+userNameArrLen, receivedTemp.length+1);
		}
		
		/**
		 * Password - if password flag is true (MSB, LSB, Value)
		 */
		int passwordArrLen = 0;
		byte[] password;
		if(flags[6]) {
			passwordArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
			// Setting
			if(passwordArrLen > 0) {
				password = Arrays.copyOfRange(receivedTemp, 2, passwordArrLen+2);
				temp.setPassword(password);
				temp.setMsbLengthforPassword(receivedTemp[0]);
				temp.setLsbLengthforPassword(receivedTemp[1]);
			}
			else {
				password = null;
			}
			// Update
			receivedTemp = Arrays.copyOfRange(receivedTemp, 2+passwordArrLen, receivedTemp.length+1);
		}
		
		return temp;
	}
	
	private Command parseConnack(Command c, byte[] received) {
		ConnackCommand temp = (ConnackCommand) c;
		byte[] receivedTemp = received;
		
		/**
		 * Remaining Length (Value)
		 */	
		// Setting
		temp.setRemainingLength(new byte[]{receivedTemp[1]}); // fixed
		// Update receivedTemp (to throw away the parsed header)
		receivedTemp = Arrays.copyOfRange(receivedTemp, 2, receivedTemp.length+1);
		
		/**
		 * Connect Acknowledge Flags (1 byte Value)
		 */
		// Setting
		temp.setAcknowledgeFlags(receivedTemp[0]);
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 1, receivedTemp.length+1);
		
		/**
		 * Connect Return Code (1 byte Value)
		 */
		// Setting
		temp.setReturnCode(receivedTemp[0]);
		
		return temp;
	}

	private Command parsePublish(Command c, byte[] received) {
		PublishCommand temp = (PublishCommand) c;
		byte[] receivedTemp = received;
		
		/**
		 * Remaining Legnth (Value)
		 */
		// Setting
		temp = (PublishCommand) updateRemainingLength(temp, receivedTemp);
		// Update receivedTemp (to throw away the parsed header)
		receivedTemp = Arrays.copyOfRange(receivedTemp, (ParseUtils.getArrayLenRL(receivedTemp))+1, receivedTemp.length+1);
		
		/**
		 * Topic Name (MSB, LSB, Value)
		 */
		int topicArrLen = ByteUtils.calcLengthMSBtoLSB(receivedTemp[0], receivedTemp[1]);
		byte[] topicName = Arrays.copyOfRange(receivedTemp, 2, topicArrLen+2);
		// Setting
		temp.setMsbLengthforTopic(receivedTemp[0]);
		temp.setLsbLengthforTopic(receivedTemp[1]);
		temp.setTopicName(topicName);
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, topicArrLen+2, receivedTemp.length+1);
		
		/**
		 * Packet Identifier (MSB, LSB)
		 */
		// Setting
		temp.setMsbLengthforPacketID(receivedTemp[0]);
		temp.setLsbLengthforPacketID(receivedTemp[1]);
		// Update
		receivedTemp = Arrays.copyOfRange(receivedTemp, 2, receivedTemp.length+1);
		
		/**
		 * Payload (Value)
		 */
		// Setting
		temp.setPayload(receivedTemp);

		return temp;
	}
	
	private Command updateRemainingLength(Command temp, byte[] receivedTemp) {
		/**
		 * Remaining Length (Value)
		 */	
		int arrayLenRL = ParseUtils.getArrayLenRL(receivedTemp);
		// Setting
		temp.setRemainingLength(Arrays.copyOfRange(receivedTemp, 1, 1+arrayLenRL));
		int decodedRL = ByteUtils.decodeRL(temp.getRemainingLength());

		// 1 = type, flag byte
		// arrayLenRL = remaining length array length  
		// decodedRL = the length of variable header and payload.
		int allRL = 1 + arrayLenRL + decodedRL;

		// Check if available packet.
		if (receivedTemp.length != allRL) {
			System.out.println("[Error] Remaining Length 부적합. Received Message Length : " + receivedTemp.length + ", Remaining Length : " + decodedRL + ".");
			return null;
		}
	

		return temp;
	}
}
