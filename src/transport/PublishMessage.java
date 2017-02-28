package transport;

import java.net.Socket;

import command.PublishCommand;
import util.ByteUtils;

public class PublishMessage {
	private int packetID;
	private byte QoS;
	private boolean dup;
	private boolean retain;
	private String topic;
	private String msg;
	private Socket socketPub;
	
	public PublishMessage(PublishCommand command, Socket socket) {
		byte ID_MSB = command.getMsbLengthforPacketID();
		byte ID_LSB = command.getLsbLengthforPacketID();
		
		packetID = ByteUtils.calcLengthMSBtoLSB(ID_MSB, ID_LSB);
		QoS = command.getQoS();
		
		topic = new String(command.getTopicName());
		msg = new String(command.getPayload());
		socketPub = socket;
		
	}
	
	public int getPacketID() {
		return packetID;
	}
	public void setPacketID(int packetID) {
		this.packetID = packetID;
	}
	public byte getQoS() {
		return QoS;
	}
	public void setQoS(byte QoS) {
		this.QoS = QoS;
	}
	public boolean isDup() {
		return dup;
	}
	public void setDup(boolean dup) {
		this.dup = dup;
	}
	public boolean isRetain() {
		return retain;
	}
	public void setRetain(boolean retain) {
		this.retain = retain;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Socket getSocketPub() {
		return socketPub;
	}
	public void setSocketPub(Socket socketPub) {
		this.socketPub = socketPub;
	}
	
	
}
