package transport;

import java.net.Socket;

public class SubscribeMessage {
	private int packetID;
	private byte requestedQoS;
	private String topic;
	private Socket socketSub;
	
	public int getPacketID() {
		return packetID;
	}
	public void setPacketID(int packetID) {
		this.packetID = packetID;
	}
	public byte getRequestedQoS() {
		return requestedQoS;
	}
	public void setRequestedQoS(byte requestedQoS) {
		this.requestedQoS = requestedQoS;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Socket getSocketSub() {
		return socketSub;
	}
	public void setSocketSub(Socket socketSub) {
		this.socketSub = socketSub;
	}
	
	
}
