package transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import client.Client;
import command.Command;
import command.ConnectCommand;
import command.PublishCommand;
import command.SubscribeCommand;
import constants.PacketType;
import parse.Parse;
import util.ByteUtils;
import util.StringUtils;

public class TCPServerConnection {

	private ArrayList<Client> arrClient = new ArrayList();
	private HashMap<String, ArrayList<Client>> hash = new HashMap<>();
	private ServerSocket ss;
	
	public void start() {
		try {
			ss = new ServerSocket(1883);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Socket accept() {
		Socket s = null;
		byte[] commBuffer = new byte[4096];
		int receiveDataSize = 0;
		try {
			s = ss.accept();
			
			InetAddress addr = s.getLocalAddress();
			String hostAddr = addr.getHostAddress();
			
			Client client = new Client();
			client.setIp(hostAddr);
			client.setSocket(s);
			arrClient.add(client);
			
			OutputStream os = s.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			InputStream is = s.getInputStream();
			java.io.DataInputStream dis = new DataInputStream(is);
			while(true) {
				receiveDataSize = dis.read(commBuffer);
				byte[] receiveData = new byte[receiveDataSize];
				for(int j=0; j<receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Parse p = new Parse();
				Command com = p.parse(receiveData);
				if(com == null)
					continue;
				if(com.getType() == PacketType.TYPE_CONNECT) {
					ConnectCommand cc = (ConnectCommand) com;
					client.setKeepAlive(ByteUtils.calcLengthMSBtoLSB(cc.getMsbKeepAlive(), cc.getLsbKeepAlive()));
					client.setClientID(new String(cc.getIdentifier()));
					client.setIp(hostAddr);
					client.setFlags(cc.getFlags());
					client.setPassword(new String(cc.getPassword()));
					client.setSocket(s);
					client.setUserName(new String(cc.getUserName()));
					client.setWillMessage(new String(cc.getWillMessage()));
					client.setWillTopic(new String(cc.getWillTopic()));
					arrClient.add(client);
					cc.print();
				} else if(com.getType() == PacketType.TYPE_PUBLISH) {
					PublishCommand pc = (PublishCommand) com;
					
					if(hash.get(new String(pc.getTopicName())) != null) {
						ArrayList<Client> tempArr = hash.get(new String(pc.getTopicName()));
						for(int i=0; i<tempArr.size(); i++)
							tempArr.get(i).getSocket().getOutputStream().write(pc.merge()) ;
					} 
				} else if(com.getType() == PacketType.TYPE_SUBSCRIBE) {
					SubscribeCommand sc = (SubscribeCommand) com;
					
					ArrayList<Byte[]> topicArr = sc.getTopicFilter();
					for(int i=0; i<topicArr.size(); i++) {
						ArrayList<Client> tempArr = hash.get(new String(ByteUtils.toPrimitives(topicArr.get(i))));
						// 더 진행해야함
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s;
	}
}
