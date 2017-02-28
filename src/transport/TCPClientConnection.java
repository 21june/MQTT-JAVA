package transport;

import java.io.IOException;
import java.net.Socket;

import command.Command;
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
import util.ByteUtils;
import util.StringUtils;

public class TCPClientConnection {

	public Socket socket = null;
	public Parse p = null;

	public void start(String serverIP) {
		p = new Parse();
		try {
			socket = new Socket(serverIP, 1883);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void subscribe(byte[] connectCommand) {
		try {
			Command c = p.parse(connectCommand);
			c.print();
			System.out.println(" ---กๆ Subscribing...");
			System.out.println("");
			socket.getOutputStream().write(connectCommand);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(byte[] connectCommand) {
		try {
			Command c = p.parse(connectCommand);
			c.print();
			System.out.println(" ---กๆ Connecting...");
			System.out.println("");
			socket.getOutputStream().write(connectCommand);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void readConnack() { try { int receiveDataSize = 0; byte[] commBuffer = new byte[4096]; byte[] receiveData; receiveDataSize =
	 * socket.getInputStream().read(commBuffer);
	 * 
	 * System.out.println("*** Waiting Connack Message from Broker...");
	 * 
	 * while (true) { if (receiveDataSize == 0) continue; receiveData = new byte[receiveDataSize]; for (int j = 0; j < receiveDataSize; j++) receiveData[j] = commBuffer[j]; Command
	 * c = p.parse(receiveData); if (c == null) continue; if (c.getType() == PacketType.TYPE_CONNACK) { c.print(); System.out.println("ก็--- Receiving Connack"); }
	 * System.out.println(""); System.out.println(">>>>>>>>> Connect Complete"); System.out.println(""); System.out.println(""); break; } } catch (IOException e) {
	 * e.printStackTrace(); } }
	 * 
	 * public void readUnsuback() { try { int receiveDataSize = 0; byte[] commBuffer = new byte[4096]; byte[] receiveData; receiveDataSize =
	 * socket.getInputStream().read(commBuffer);
	 * 
	 * System.out.println("*** Waiting Unsuback Message from Broker...");
	 * 
	 * while (true) { if (receiveDataSize == 0) continue; receiveData = new byte[receiveDataSize]; for (int j = 0; j < receiveDataSize; j++) receiveData[j] = commBuffer[j]; Command
	 * c = p.parse(receiveData); if (c == null) continue; if (c.getType() == PacketType.TYPE_UNSUBACK) { c.print(); System.out.println("ก็--- Receiving Unsuback"); }
	 * System.out.println(""); System.out.println(">>>>>>>>> Unsubscribe Complete!"); System.out.println(""); System.out.println(""); break; } } catch (IOException e) {
	 * e.printStackTrace(); } }
	 * 
	 * public void readSuback() { try { int receiveDataSize = 0; byte[] commBuffer = new byte[4096]; byte[] receiveData; receiveDataSize = socket.getInputStream().read(commBuffer);
	 * 
	 * System.out.println("*** Waiting Suback Message from Broker...");
	 * 
	 * while (true) { if (receiveDataSize == 0) continue; receiveData = new byte[receiveDataSize]; for (int j = 0; j < receiveDataSize; j++) receiveData[j] = commBuffer[j]; Command
	 * c = p.parse(receiveData); if (c == null) continue; if (c.getType() == PacketType.TYPE_SUBACK) { c.print(); System.out.println("ก็--- Receiving Suback"); }
	 * System.out.println(""); System.out.println(">>>>>>>>> Subscribe Complete!"); System.out.println(""); System.out.println(""); break; } } catch (IOException e) {
	 * e.printStackTrace(); } }
	 * 
	 * public void readPublish() { try { int receiveDataSize = 0; byte[] commBuffer = new byte[4096]; byte[] receiveData; receiveDataSize =
	 * socket.getInputStream().read(commBuffer);
	 * 
	 * while (true) { if (receiveDataSize == 0) continue; receiveData = new byte[receiveDataSize]; for (int j = 0; j < receiveDataSize; j++) receiveData[j] = commBuffer[j]; Command
	 * c = p.parse(receiveData); if (c == null) continue; if (c.getType() == PacketType.TYPE_PUBLISH) { c.print(); PublishCommand pc = (PublishCommand) c; byte qos = pc.getQoS();
	 * System.out.println("PUBLISH QoS: " + qos); if (qos == 1) { PubackCommand pack = new PubackCommand(); pack.setMsbIdentifier(pc.getMsbLengthforPacketID());
	 * pack.setLsbIdentifier(pc.getLsbLengthforPacketID()); socket.getOutputStream().write(pack.merge()); System.out.println("**QoS1: PUBACK Send**"); } else if (qos == 2) {
	 * PubrecCommand prec = new PubrecCommand(); byte _packet_msb = pc.getMsbLengthforPacketID(); byte _packet_lsb = pc.getLsbLengthforPacketID();
	 * prec.setMsbPakcetIdentifier(_packet_msb); prec.setLsbPacketIdentifier(_packet_lsb); socket.getOutputStream().write(prec.merge());
	 * System.out.println("**QoS2: PUBREC Send**");
	 * 
	 * byte[] temp = new byte[4096]; socket.getInputStream().read(temp);
	 * 
	 * Parse tempParse = new Parse(); Command _tempC = tempParse.parse(temp);
	 * 
	 * if (_tempC.getType() == PacketType.TYPE_PUBREL) { PubrelCommand prel = (PubrelCommand) _tempC; System.out.println("**QoS2: PUBREL Receive**"); if (_packet_msb ==
	 * prel.getMsbPacketIdentifier() && _packet_lsb == prel.getLsbPacketIdentifier()) { PubcompCommand pcomp = new PubcompCommand(); pcomp.setMsbPacketIdentifier(_packet_msb);
	 * pcomp.setLsbPacketIdentifier(_packet_lsb); socket.getOutputStream().write(pcomp.merge()); System.out.println("**QoS2: PUBCOMP Send**"); }
	 * 
	 * } } System.out.println("ก็--- Received Publish Message from \"" + new String(pc.getTopicName()) + "\""); } System.out.println(""); System.out.println("");
	 * System.out.println(""); break; } } catch (IOException e) { e.printStackTrace(); } }
	 * 
	 * public void readPuback() { try { int receiveDataSize = 0; byte[] commBuffer = new byte[4096]; byte[] receiveData; receiveDataSize = socket.getInputStream().read(commBuffer);
	 * 
	 * System.out.println("*** Waiting Puback Message from Broker...");
	 * 
	 * while (true) { if (receiveDataSize == 0) continue; receiveData = new byte[receiveDataSize]; for (int j = 0; j < receiveDataSize; j++) receiveData[j] = commBuffer[j]; Command
	 * c = p.parse(receiveData); if (c == null) continue; if (c.getType() == PacketType.TYPE_PUBACK) { c.print(); System.out.println("ก็--- Receiving Puback"); }
	 * System.out.println(""); System.out.println(">>>>>>>>> Publish Complete!"); System.out.println(""); System.out.println(""); break; } } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */
	public void send(byte[] sendData) {
		try {
			System.out.println(" ----กๆ Sending...");
			socket.getOutputStream().write(sendData);
			socket.getOutputStream().flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("");
		System.out.println("");
	}

	/**
	 * main.main.MODE => Current Type
	 */
	public void read() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			if (receiveDataSize == -1)
				return;

			receiveData = new byte[receiveDataSize];
			for (int j = 0; j < receiveDataSize; j++)
				receiveData[j] = commBuffer[j];
			Command c = p.parse(receiveData);
			if (c == null && main.main.MODE == 0)
				return;
			if (c == null)
				return;

			/**
			 * PUBLISH RECEIVER
			 */
			if (c.getType() == PacketType.TYPE_PUBLISH) {
				main.main.sc.close();
				c.print();
				PublishCommand pc = (PublishCommand) c;

				StringUtils.printByteArray(pc.merge());
				byte _msb = pc.getMsbLengthforPacketID();
				byte _lsb = pc.getLsbLengthforPacketID();
				byte qos = pc.getQoS();

				System.out.println(" [RECEIVER] QOS: " + qos);

				/**
				 * QoS1 Sequence 1. Receive PUBLISH. (Completed) 2. Send PUBACK.
				 */
				if (qos == 1) {
					PubackCommand pack = new PubackCommand();
					pack.init();
					pack.setMsbIdentifier(_msb);
					pack.setLsbIdentifier(_lsb);
					socket.getOutputStream().write(pack.merge());
					System.out.println("[RECEIVER] QoS1: 1. PUBACK Send");
					System.out.println("[RECEIVER] PUBLISH COMPLTED!");
				}

				/**
				 * QOS2 Sequence 1. Receive PUBLISH. (Completed) 2. Send PUBREC. 3. Receive PUBREL. 4. Send PUBCOMP.
				 */
				else if (qos == 2) {
					PubrecCommand prec = new PubrecCommand();
					prec.init();
					prec.setMsbPakcetIdentifier(_msb);
					prec.setLsbPacketIdentifier(_lsb);
					socket.getOutputStream().write(prec.merge());
					System.out.println("**[RECEIVER] QoS2: 1. PUBREC Send**");

					byte[] temp = new byte[4096];
					socket.getInputStream().read(temp);

					Parse tempParse = new Parse();
					Command _parse_cmd = tempParse.parse(temp);

					if (_parse_cmd.getType() == PacketType.TYPE_PUBREL) {
						PubrelCommand prel = (PubrelCommand) _parse_cmd;
						System.out.println("**[RECEIVER] QoS2: 2. PUBREL Receive**");
						if (_msb == prel.getMsbPacketIdentifier() && _lsb == prel.getLsbPacketIdentifier()) {
							PubcompCommand pcomp = new PubcompCommand();
							pcomp.init();
							pcomp.setMsbPacketIdentifier(_msb);
							pcomp.setLsbPacketIdentifier(_lsb);
							socket.getOutputStream().write(pcomp.merge());
							System.out.println("**[RECEIVER] QoS2: 3. PUBCOMP Receive**");
						}
					}

					System.out.println("[RECEIVER] PUBLISH COMPLTED!");
				}
				main.main.displayPublishPopup(new String(pc.getPayload()));
				main.main.printOption(this, main.main.sc);
				return;
			}

			/**
			 * Apply Next Sequence> ex) if PUBLISH (QoS1), you should receive PUBACK in case PUBLISH.
			 */
			byte _msb;
			byte _lsb;
			switch (main.main.MODE) {
			case PacketType.TYPE_CONNECT:
				if (c.getType() == PacketType.TYPE_CONNACK) {
					System.out.println("ก็--- Received Connack");
					main.main.MODE = 0;
					c.print();
					main.main.printOption(this, main.main.sc);
					main.main.mf.setInitUI();
					return;
				}
				break;

			case PacketType.TYPE_CONNACK:
				System.out.println("ก็--- Received Connack");
				break;

			/**
			 * PUBLISH SENDER
			 */
			case PacketType.TYPE_PUBLISH:
				PublishCommand pc = (PublishCommand) main.main.RECENT_COMMAND;
				byte qos = pc.getQoS();
				_msb = pc.getMsbLengthforPacketID();
				_lsb = pc.getLsbLengthforPacketID();
				if (qos == 1) {
					/**
					 * QoS1 Sequence 1. Send PUBLISH. (Completed) 2. Receive PUBACK. (To do)
					 */
					if (c.getType() == PacketType.TYPE_PUBACK) {
						PubackCommand pack = (PubackCommand) c;
						byte _pack_msb = pack.getMsbIdentifier();
						byte _pack_lsb = pack.getLsbIdentifier();

						if (_pack_msb == _msb && _pack_lsb == _lsb) {
							System.out.println("ก็--- Received Puback");
							System.out.println("**[SENDER] Qos1: 1. PUBACK Receive**");
							System.out.println("[SENDER] PUBLISH COMPLTED!");
							main.main.MODE = 0;
							c.print();
							main.main.printOption(this, main.main.sc);
							return;
						}
					}
				}

				/**
				 * QoS2 Sequence 1. Send PUBLISH. (Completed) 2. Receive PUBREC 3. Send PUBREL. 4. Receive PUBCOMP.
				 */
				else if (qos == 2) {
					if (c.getType() == PacketType.TYPE_PUBREC) {
						PubrecCommand prec = (PubrecCommand) c;
						byte _prec_msb = prec.getMsbPakcetIdentifier();
						byte _prec_lsb = prec.getLsbPacketIdentifier();

						if (_prec_msb == _msb && _prec_lsb == _lsb) {
							System.out.println("ก็--- Received Pubrec");
							System.out.println("**[SENDER] Qos2: 1. PUBREC Receive**");
							System.out.println("WAITING PUBCOMP MESSAGE...");
							PubrelCommand prel = new PubrelCommand();
							prel.init();
							prel.setMsbPacketIdentifier(_msb);
							prel.setLsbPacketIdentifier(_lsb);
							socket.getOutputStream().write(prel.merge());
							System.out.println("**[SENDER] Qos2: 2. PUBREL Send**");
							c.print();
							return;
						}
					} else if (c.getType() == PacketType.TYPE_PUBCOMP) {
						PubcompCommand pcom = (PubcompCommand) c;
						byte _pcom_msb = pcom.getMsbPacketIdentifier();
						byte _pcom_lsb = pcom.getLsbPacketIdentifier();

						if (_pcom_msb == _msb && _pcom_lsb == _lsb) {
							System.out.println("ก็--- Received Pubcomp");
							System.out.println("**[SENDER] Qos2: 3. PUBCOMP Receive**");
							System.out.println("[SENDER] PUBLISH COMPLTED!");
							main.main.MODE = 0;
							c.print();
							main.main.printOption(this, main.main.sc);
						}
					}
				}
				break;

			case PacketType.TYPE_PUBACK:
				break;

			case PacketType.TYPE_PUBREC:
				System.out.println("ก็--- Receiving Pubrec");
				break;

			case PacketType.TYPE_PUBREL:
				System.out.println("ก็--- Receiving Pubrel");
				break;

			case PacketType.TYPE_PUBCOMP:
				System.out.println("ก็--- Receiving Pubcomp");
				break;

			case PacketType.TYPE_SUBSCRIBE:
				if (c.getType() == PacketType.TYPE_SUBACK) {
					SubscribeCommand sc = (SubscribeCommand) main.main.RECENT_COMMAND;
					SubackCommand sack = (SubackCommand) c;
					_msb = sc.getMsbLengthforPacketID();
					_lsb = sc.getLsbLengthforPacketID();

					byte _sack_msb = sack.getMsbLengthforPacketID();
					byte _sack_lsb = sack.getLsbLengthforPacketID();

					if (_msb == _sack_msb && _lsb == _sack_lsb) {
						System.out.println("ก็--- Received Suback");
						main.main.MODE = 0;
						c.print();
						main.main.printOption(this, main.main.sc);
						return;
					}
				}
				break;

			case PacketType.TYPE_SUBACK:
				break;

			case PacketType.TYPE_UNSUBSCRIBE:
				if (c.getType() == PacketType.TYPE_UNSUBACK) {
					UnsubscribeCommand uc = (UnsubscribeCommand) main.main.RECENT_COMMAND;
					UnsubackCommand uack = (UnsubackCommand) c;
					_msb = uc.getMsbLengthforPacketID();
					_lsb = uc.getLsbLengthforPacketID();

					byte _uack_msb = uack.getMsbPacketIdentifier();
					byte _uack_lsb = uack.getLsbPacketIdentifier();

					if (_msb == _uack_msb && _lsb == _uack_lsb) {
						System.out.println("ก็--- Received Unsuback");
						main.main.MODE = 0;
						c.print();
						main.main.printOption(this, main.main.sc);
						return;
					}
				}
				break;

			case PacketType.TYPE_UNSUBACK:
				break;

			case PacketType.TYPE_PINGREQ:
				System.out.println("ก็--- Receiving Pingreq");
				break;

			case PacketType.TYPE_PINGRESP:
				System.out.println("ก็--- Receiving Pingresp");
				break;

			case PacketType.TYPE_DISCONNECT:
				System.out.println("ก็--- Receiving Disconnect");
				break;

			case 0:
			default:
				System.out.println("ก็--- Receiving packet in MODE 0");
				return;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
