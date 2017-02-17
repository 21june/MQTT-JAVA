package transport;

import java.io.IOException;
import java.net.Socket;

import command.Command;
import command.PublishCommand;
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

	public void readConnack() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			System.out.println("*** Waiting Connack Message from Broker...");

			while (true) {
				if (receiveDataSize == 0)
					continue;
				receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if (c == null)
					continue;
				if (c.getType() == PacketType.TYPE_CONNACK) {
					c.print();
					System.out.println("ก็--- Receiving Connack");
				}
				System.out.println("");
				System.out.println(">>>>>>>>> Connect Complete");
				System.out.println("");
				System.out.println("");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readUnsuback() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			System.out.println("*** Waiting Unsuback Message from Broker...");

			while (true) {
				if (receiveDataSize == 0)
					continue;
				receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if (c == null)
					continue;
				if (c.getType() == PacketType.TYPE_UNSUBACK) {
					c.print();
					System.out.println("ก็--- Receiving Unsuback");
				}
				System.out.println("");
				System.out.println(">>>>>>>>> Unsubscribe Complete!");
				System.out.println("");
				System.out.println("");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readSuback() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			System.out.println("*** Waiting Suback Message from Broker...");

			while (true) {
				if (receiveDataSize == 0)
					continue;
				receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if (c == null)
					continue;
				if (c.getType() == PacketType.TYPE_SUBACK) {
					c.print();
					System.out.println("ก็--- Receiving Suback");
				}
				System.out.println("");
				System.out.println(">>>>>>>>> Subscribe Complete!");
				System.out.println("");
				System.out.println("");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readPublish() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			while (true) {
				if (receiveDataSize == 0)
					continue;
				receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if (c == null)
					continue;
				if (c.getType() == PacketType.TYPE_PUBLISH) {
					c.print();
					PublishCommand pc = (PublishCommand) c;
					System.out.println("ก็--- Received Publish Message from \"" + new String(pc.getTopicName()) + "\"");
				}
				System.out.println("");
				System.out.println("");
				System.out.println("");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readPuback() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			System.out.println("*** Waiting Puback Message from Broker...");

			while (true) {
				if (receiveDataSize == 0)
					continue;
				receiveData = new byte[receiveDataSize];
				for (int j = 0; j < receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if (c == null)
					continue;
				if (c.getType() == PacketType.TYPE_PUBACK) {
					c.print();
					System.out.println("ก็--- Receiving Puback");
				}
				System.out.println("");
				System.out.println(">>>>>>>>> Publish Complete!");
				System.out.println("");
				System.out.println("");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] sendData) {
		try {

			ByteUtils.convertBytetoBits(sendData[0]);

			Command c = p.parse(sendData);
			c.print();
			System.out.println(" ----กๆ Sending...");
			socket.getOutputStream().write(sendData);
			socket.getOutputStream().flush();

			StringUtils.printByteArray(sendData);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("");
		System.out.println("");
	}

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

			System.out.println("1");
			if (c.getType() == PacketType.TYPE_PUBLISH) {
				main.main.sc.close();
				c.print();
				PublishCommand pc = (PublishCommand) c;
				main.main.displayPublishPopup(new String(pc.getPayload()));
				main.main.printOption(this, main.main.sc);
				return;
			}

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

			case PacketType.TYPE_PUBLISH:
				if (c.getType() == PacketType.TYPE_PUBACK) {
					System.out.println("ก็--- Received Puback");
					main.main.MODE = 0;
					c.print();
					main.main.printOption(this, main.main.sc);
					return;
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
					System.out.println("ก็--- Received Suback");
					main.main.MODE = 0;
					c.print();
					main.main.printOption(this, main.main.sc);
					return;
				}
				break;

			case PacketType.TYPE_SUBACK:
				break;

			case PacketType.TYPE_UNSUBSCRIBE:
				if (c.getType() == PacketType.TYPE_UNSUBACK) {
					System.out.println("ก็--- Received Unsuback");
					main.main.MODE = 0;
					c.print();
					main.main.printOption(this, main.main.sc);
					return;
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
				System.out.println("ก็--- Receiving No type");
				return;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
}
