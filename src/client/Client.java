package client;

import java.net.Socket;
import java.util.ArrayList;

public class Client {
	// Subscribe
	ArrayList<String> subTopic = new ArrayList<String>();
	ArrayList<Byte> subRequestedQoS = new ArrayList<Byte>();
	
	// Flag
	boolean[] flags = new boolean[8];
	int keepAlive = 0;
	String clientID;
	String willTopic;
	String willMessage;
	String userName;
	String password;
	
	// User
	String ip;
	Socket socket;
	
	

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket s) {
		this.socket = s;
	}

	public ArrayList<String> getSubTopic() {
		return subTopic;
	}

	public void setSubTopic(ArrayList<String> subTopic) {
		this.subTopic = subTopic;
	}

	public ArrayList<Byte> getSubRequestedQoS() {
		return subRequestedQoS;
	}

	public void setSubQoS(ArrayList<Byte> subRequestedQoS) {
		this.subRequestedQoS = subRequestedQoS;
	}

	public boolean[] getFlags() {
		return flags;
	}

	public void setFlags(boolean[] flags) {
		this.flags = flags;
	}

	public int getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(int keepAlive) {
		this.keepAlive = keepAlive;
	}

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getWillTopic() {
		return willTopic;
	}

	public void setWillTopic(String willTopic) {
		this.willTopic = willTopic;
	}

	public String getWillMessage() {
		return willMessage;
	}

	public void setWillMessage(String willMessage) {
		this.willMessage = willMessage;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	
	
}
