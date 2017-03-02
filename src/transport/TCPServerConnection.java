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
import command.PubackCommand;
import command.PubcompCommand;
import command.PublishCommand;
import command.PubrecCommand;
import command.PubrelCommand;
import command.SubackCommand;
import command.SubscribeCommand;
import command.UnsubackCommand;
import command.UnsubscribeCommand;
import constants.PacketType;
import parse.Parse;
import util.BoolUtils;
import util.ByteUtils;
import util.StringUtils;

public class TCPServerConnection {

	public static int MODE = 0;
	private ServerSocket ss;
	private Socket clientSock = null;

	public void start() {
		try {
			ss = new ServerSocket(1883);
			System.out.println("[Server Socket] Start");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void read(Socket sock) {
		Socket s = sock;
		byte[] commBuffer = new byte[4096];
		int receiveDataSize = 0;
		try {
			InetAddress addr = s.getLocalAddress();
			String hostAddr = addr.getHostAddress();

			Client client = new Client();
			client.setIp(hostAddr);
			client.setSocket(s);
			ServerData.arrClient.add(client);

			OutputStream os = s.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			InputStream is = s.getInputStream();
			java.io.DataInputStream dis = new DataInputStream(is);
			while (true) {
				receiveDataSize = dis.read(commBuffer);
				if (receiveDataSize < 0)
					continue;
				byte[] receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Parse p = new Parse();
				Command com = p.parse(receiveData);
				if (com == null)
					continue;

				String labelText = main.main.brokerLabel.getText();
				main.main.brokerLabel.setText(labelText + new String(com.merge()));
				if (com.getType() == PacketType.TYPE_CONNECT) {
					ConnectCommand cc = (ConnectCommand) com;
					client.setKeepAlive(ByteUtils.calcLengthMSBtoLSB(cc.getMsbKeepAlive(), cc.getLsbKeepAlive()));
					client.setClientID(new String(cc.getIdentifier()));
					client.setIp(hostAddr);
					client.setFlags(cc.getFlags());
					System.out.println("[BROKER] CONNECT COMMAND RECEIVED from '" + client.getClientID() + "'");

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
					System.out.println("[BROKER] CONNACK COMMAND SENT to '" + client.getClientID() + "'");
				} else if (com.getType() == PacketType.TYPE_PUBLISH) {
					System.out.println("[BROKER] PUBLISH COMMAND RECEIVED from '" + client.getClientID() + "'");

					PublishCommand pc = (PublishCommand) com;

					if (pc.getQoS() == 1) {
						// WRITE PUBACK to PUBLISHER
						PubackCommand pack = new PubackCommand();
						pack.init();
						pack.setLsbIdentifier(pc.getLsbLengthforPacketID());
						pack.setMsbIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(pack.merge());
						System.out.println("[PUBLISH/RECEIVER/QoS1] WRITING PUBACK to '" + client.getClientID() + "'");
					}

					else if (pc.getQoS() == 2) {
						// WRITE PUBREC to PUBLISHER
						PubrecCommand prec = new PubrecCommand();
						prec.init();
						prec.setLsbPacketIdentifier(pc.getLsbLengthforPacketID());
						prec.setMsbPakcetIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(prec.merge());
						System.out.println("[PUBLISH/RECEIVER/QoS2] 1. WRITING PUBREC to '" + client.getClientID() + "'");

						// READ PUBREL from PUBLISHER
						while (true) {
							int receivePubrelSize = 0;
							byte[] pubrelBuffer = new byte[4096];
							byte[] receivePubrel;
							receivePubrelSize = s.getInputStream().read(pubrelBuffer);

							if (receivePubrelSize == -1)
								continue;
							receivePubrel = new byte[receivePubrelSize];
							for (int i = 0; i < receivePubrelSize; i++)
								receivePubrel[i] = pubrelBuffer[i];
							Command c = p.parse(receivePubrel);
							if (c != null & c.getType() == PacketType.TYPE_PUBREL) {
								System.out.println("[PUBLISH/RECEIVER/QoS2] 2. READING PUBREL from '" + client.getClientID() + "'");
								break;
							}
						}

						// WRITE PUBCOMP to PUBLISHER
						PubcompCommand pcomp = new PubcompCommand();
						pcomp.init();
						pcomp.setLsbPacketIdentifier(pc.getLsbLengthforPacketID());
						pcomp.setMsbPacketIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(pcomp.merge());
						System.out.println("[PUBLISH/RECEIVER/QoS2] 3. WRITING PUBCOMP to '" + client.getClientID() + "'");
					}

					ServerData.hashPubMsg.get(new String(pc.getPayload()));
					// pc.print();

					// Search Client that subscribes the topic
					// then send PUBLISH MESSAGE
					if (ServerData.hashClient.get(new String(pc.getTopicName())) != null) {
						System.out.println("[PUBLISH/SENDER] Topic: " + new String(pc.getTopicName()));
						ArrayList<Client> tempArr = ServerData.hashClient.get(new String(pc.getTopicName()));
						if (tempArr != null) {
							for (int i = 0; i < tempArr.size(); i++) {
								byte pubQoS = pc.getQoS();
								ArrayList<String> strTemp = tempArr.get(i).getSubTopic();
								int index = -1;
								for (int j = 0; j < strTemp.size(); j++)
									if (strTemp.get(j).equals(new String(pc.getTopicName())))
										index = j;
								byte subQoS = 0;
								if (index == -1)
									subQoS = pubQoS;
								else
									subQoS = tempArr.get(i).getSubRequestedQoS().get(index);

								byte resultQoS = 0;
								if (pubQoS == subQoS)
									resultQoS = subQoS;
								else if (pubQoS > subQoS)
									resultQoS = subQoS;
								else
									resultQoS = pubQoS;

								PublishCommand toClient = (PublishCommand) pc;
								toClient.setQoS(BoolUtils.getBoolQoS(resultQoS));
								System.out.println("[PUBLISH/SENDER] PUBLISH to " + tempArr.get(i).getClientID());
								tempArr.get(i).getSocket().getOutputStream().write(toClient.merge());
							}
						}
					}
				}

				else if (com.getType() == PacketType.TYPE_SUBSCRIBE) {
					System.out.println("[BROKER] SUBSCRIBE COMMAND RECEIVED from '" + client.getClientID() + "'");
					SubscribeCommand sc = (SubscribeCommand) com;
					// sc.print();

					ArrayList<Byte[]> topicArr = sc.getTopicFilter();
					ArrayList<Byte> qosArr = sc.getQos();
					for (int i = 0; i < topicArr.size(); i++) {
						String topic = new String(ByteUtils.toPrimitives(topicArr.get(i)));
						ArrayList<Client> tempArr = ServerData.hashClient.get(topic);
						System.out.println("[BROKER] SUBSCRIBE TOPIC : " + new String(topic));
						if (tempArr != null) {
							tempArr.add(client);
						} else {
							tempArr = new ArrayList<Client>();
							tempArr.add(client);
						}
						ServerData.hashClient.put(topic, tempArr);
					}
					SubackCommand sack = new SubackCommand();
					sack.init();
					sack.setMsbLengthforPacketID(sc.getMsbLengthforPacketID());
					sack.setLsbLengthforPacketID(sc.getLsbLengthforPacketID());
					byte[] returnCodes = new byte[topicArr.size()];
					for (int i = 0; i < returnCodes.length; i++) {
						if (qosArr.get(i) == 0)
							returnCodes[i] = 0;
						else if (qosArr.get(i) == 1)
							returnCodes[i] = 1;
						else if (qosArr.get(i) == 2)
							returnCodes[i] = 2;
						else
							returnCodes[i] = (byte) 128;
					}
					sack.setReturncode(returnCodes);
					s.getOutputStream().write(sack.merge());
					System.out.println("[BROKER] SUBACK COMMANDS SENT to '" + client.getClientID() + "'");
				}

				else if (com.getType() == PacketType.TYPE_UNSUBSCRIBE) {
					System.out.println("[BROKER] UNSUBSCRIBE COMMAND RECEIVED from '" + client.getClientID() + "'");
					String cID = client.getClientID();
					UnsubscribeCommand unsub = (UnsubscribeCommand) com;
					ArrayList<Byte[]> unsubTopics = unsub.getTopicFilter();
					String topic = null;

					// Remove unsubscription topic from server data
					for(int i=0; i<unsubTopics.size(); i++) {
						topic = new String(ByteUtils.toPrimitives(unsubTopics.get(i)));
						ArrayList<Client> arrClient = ServerData.hashClient.get(topic);
						for(int j=0; j<arrClient.size(); j++) {
							if(arrClient.get(j).getClientID() == cID) {
								arrClient.remove(j);			
								ServerData.hashClient.put(topic, arrClient);
								System.out.println("[BROKER] UNSUBSCRIBED TOPIC:" + topic +", ClientID:" + cID);
							}
						}
					}

					// Send UNSUBACK Message to Client.
					UnsubackCommand unack = new UnsubackCommand();
					unack.init();
					unack.setMsbPacketIdentifier(unsub.getMsbLengthforPacketID());
					unack.setLsbPacketIdentifier(unsub.getLsbLengthforPacketID());
					s.getOutputStream().write(unack.merge());
					System.out.println("[BROKER] UNSUBACK COMMANDS SENT to '" + client.getClientID() + "'");
				}

				else if (com.getType() == PacketType.TYPE_PUBACK) {
					// READ PUBACK from SUBSCRIBER
					System.out.println("[PUBLISH/RECEIVER/QoS1] PUBACK RECEIVED! '" + client.getClientID() + "' [QoS1]");
					System.out.println("[PUBLISH/RECEIVER/QoS1] PUBLISH TO '" + client.getClientID() + "' COMPLETED! [QoS1]");
				} else if (com.getType() == PacketType.TYPE_PUBREC) {
					// READ PUBREC from SUBSCRIBER
					PubrecCommand prec = (PubrecCommand) com;
					System.out.println("[BROKER] PUBREC RECEIVED! '" + client.getClientID() + "' [QoS2]");
					PubrelCommand prel = new PubrelCommand();
					prel.init();
					prel.setLsbPacketIdentifier(prec.getLsbPacketIdentifier());
					prel.setMsbPacketIdentifier(prec.getMsbPakcetIdentifier());
					s.getOutputStream().write(prel.merge());
					System.out.println("[BROKER] PUBREL SENT! '" + client.getClientID() + "' [QoS2]");
				} else if (com.getType() == PacketType.TYPE_PUBCOMP) {
					// READ PUBCOMP from SUBSCRIBER
					System.out.println("[BROKER] PUBCOMP RECEIVED! '" + client.getClientID() + "' [QoS2]");
					System.out.println("[BROKER] PUBLISH TO '" + client.getClientID() + "' COMPLETED! [QoS2]");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void acceptServer() {
		int i = 0;
		while (true) {
			try {
				if ((clientSock = ss.accept()) != null) {
					System.out.println("[BROKER] ACCEPT Client " + ss.getInetAddress().getLocalHost().getHostAddress());
					Runnable run = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							read(clientSock);
						}
					};
					Thread th = new Thread(run);
					th.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

				String labelText = main.main.brokerLabel.getText();
				main.main.brokerLabel.setText(labelText + new String(com.merge()));
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

					if (pc.getQoS() == 1) {
						// WRITE PUBACK to PUBLISHER
						PubackCommand pack = new PubackCommand();
						pack.init();
						pack.setLsbIdentifier(pc.getLsbLengthforPacketID());
						pack.setMsbIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(pack.merge());
					}

					else if (pc.getQoS() == 2) {
						// WRITE PUBREC to PUBLISHER
						PubrecCommand prec = new PubrecCommand();
						prec.init();
						prec.setLsbPacketIdentifier(pc.getLsbLengthforPacketID());
						prec.setMsbPakcetIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(prec.merge());

						// READ PUBREL from PUBLISHER
						while (true) {
							int receivePubrelSize = 0;
							byte[] pubrelBuffer = new byte[4096];
							byte[] receivePubrel;
							receivePubrelSize = s.getInputStream().read(pubrelBuffer);

							if (receivePubrelSize == -1)
								continue;
							receivePubrel = new byte[receivePubrelSize];
							for (int i = 0; i < receivePubrelSize; i++)
								receivePubrel[i] = pubrelBuffer[i];
							Command c = p.parse(receivePubrel);
							if (c != null & c.getType() == PacketType.TYPE_PUBREL)
								break;
						}

						// WRITE PUBCOMP to PUBLISHER
						PubcompCommand pcomp = new PubcompCommand();
						pcomp.init();
						pcomp.setLsbPacketIdentifier(pc.getLsbLengthforPacketID());
						pcomp.setMsbPacketIdentifier(pc.getMsbLengthforPacketID());
						s.getOutputStream().write(pcomp.merge());
					}

					ServerData.hashPubMsg.get(new String(pc.getPayload()));
					// pc.print();

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
				}

				else if (com.getType() == PacketType.TYPE_SUBSCRIBE) {
					System.out.println("[Broker] SUBSCRIBE");
					SubscribeCommand sc = (SubscribeCommand) com;
					// sc.print();

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

				else if (com.getType() == PacketType.TYPE_PUBACK) {
					// READ PUBACK from SUBSCRIBER
					System.out.println("[BROKER] PUBACK RECEIVED! '" + client.getClientID() + "' [QoS1]");
					System.out.println("[BROKER] PUBLISH TO '" + client.getClientID() + "' COMPLETED! [QoS1]");
				} else if (com.getType() == PacketType.TYPE_PUBREC) {
					// READ PUBREC from SUBSCRIBER
					PubrecCommand prec = (PubrecCommand) com;
					System.out.println("[BROKER] PUBREC RECEIVED! '" + client.getClientID() + "' [QoS2]");
					PubrelCommand prel = new PubrelCommand();
					prel.init();
					prel.setLsbPacketIdentifier(prec.getLsbPacketIdentifier());
					prel.setMsbPacketIdentifier(prec.getMsbPakcetIdentifier());
					s.getOutputStream().write(prel.merge());
					System.out.println("[BROKER] PUBREL SENT! '" + client.getClientID() + "' [QoS2]");
				} else if (com.getType() == PacketType.TYPE_PUBCOMP) {
					// READ PUBCOMP from SUBSCRIBER
					System.out.println("[BROKER] PUBCOMP RECEIVED! '" + client.getClientID() + "' [QoS2]");
					System.out.println("[BROKER] PUBLISH TO '" + client.getClientID() + "' COMPLETED! [QoS2]");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return s;
	}
}
