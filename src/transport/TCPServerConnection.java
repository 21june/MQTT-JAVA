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
import command.ConnackCommand;
import command.ConnectCommand;
import command.PublishCommand;
import command.SubscribeCommand;
import constants.PacketType;
import parse.Parse;
import util.ByteUtils;
import util.StringUtils;

public class TCPServerConnection {

	private ServerSocket ss;

	public void start() {
		try {
			ss = new ServerSocket(1883);
			System.out.println("[Broker] Start.");
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
			ServerData.arrClient.add(client);

			System.out.println("[Broker] " + hostAddr + " Accept! ");

			OutputStream os = s.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			InputStream is = s.getInputStream();
			java.io.DataInputStream dis = new DataInputStream(is);
			while (true) {
				receiveDataSize = dis.read(commBuffer);
				byte[] receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Parse p = new Parse();
				Command com = p.parse(receiveData);
				if (com == null)
					continue;
				if (com.getType() == PacketType.TYPE_CONNECT) {
					System.out.println("[Broker] CONNECT");
					ConnectCommand cc = (ConnectCommand) com;
					client.setKeepAlive(ByteUtils.calcLengthMSBtoLSB(cc.getMsbKeepAlive(), cc.getLsbKeepAlive()));
					client.setClientID(new String(cc.getIdentifier()));
					client.setIp(hostAddr);
					client.setFlags(cc.getFlags());

					if (cc.getPassword() != null)
						client.setPassword(new String(cc.getPassword()));
					if (cc.getUserName() != null)
						client.setUserName(new String(cc.getUserName()));
					if (cc.getWillMessage() != null)
						client.setWillMessage(new String(cc.getWillMessage()));
					if (cc.getWillTopic() != null)
						client.setWillTopic(new String(cc.getWillTopic()));

					client.setSocket(s);
					ServerData.arrClient.add(client);

					ConnackCommand con = new ConnackCommand();
					con.init();
					con.setReturnCode((byte) 0);
					con.setAcknowledgeFlags((byte) 0);
					byte[] tempBytes = con.merge();

					s.getOutputStream().write(tempBytes);
					System.out.println("[Broker] CONNACK SEND!");
				} else if (com.getType() == PacketType.TYPE_PUBLISH) {
					System.out.println("[Broker] PUBLISH");
					PublishCommand pc = (PublishCommand) com; 
					ServerData.hashPubMsg.get(new String(pc.getPayload()));
					pc.print();
 
					if (ServerData.hashClient.get(new String(pc.getTopicName())) != null) {
						System.out.println("[Broker] Topic: " + new String(pc.getTopicName()));
						ArrayList<Client> tempArr = ServerData.hashClient.get(new String(pc.getTopicName()));
						if (tempArr != null) {
							for (int i = 0; i < tempArr.size(); i++) {
								System.out.println("PUBLUSH HOST: " + tempArr.get(i).getSocket().getInetAddress().getHostAddress());
								tempArr.get(i).getSocket().getOutputStream().write(pc.merge());
							}
						}
					}
				} else if (com.getType() == PacketType.TYPE_SUBSCRIBE) {
					System.out.println("[Broker] SUBSCRIBE");
					SubscribeCommand sc = (SubscribeCommand) com;
					sc.print();
					
					ArrayList<Byte[]> topicArr = sc.getTopicFilter();
					for (int i = 0; i < topicArr.size(); i++) {
						String topic = new String(ByteUtils.toPrimitives(topicArr.get(i)));
						ArrayList<Client> tempArr = ServerData.hashClient.get(topic);
						System.out.println("[Broker] Topic: " + new String(topic));
						if (tempArr != null) {
							tempArr.add(client);
						} else {
							tempArr = new ArrayList<Client>();
							tempArr.add(client);
						}
						ServerData.hashClient.put(topic, tempArr);
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
