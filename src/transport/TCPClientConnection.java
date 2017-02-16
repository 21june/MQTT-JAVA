package transport;

import java.io.IOException;
import java.net.Socket;

import command.Command;
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
			System.out.println(" ---¡æ Subscribing...");
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
			System.out.println(" ---¡æ Connecting...");
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
			
			while(true) {
				if(receiveDataSize == 0) continue;
				receiveData = new byte[receiveDataSize];
				for(int j=0; j<receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if(c == null) continue;
				if(c.getType() == PacketType.TYPE_CONNACK) {
					c.print();
					System.out.println("¡ç--- Receiving Connack");
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
			
			while(true) {
				if(receiveDataSize == 0) continue;
				receiveData = new byte[receiveDataSize];
				for(int j=0; j<receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if(c == null) continue;
				if(c.getType() == PacketType.TYPE_UNSUBACK) {
					c.print();
					System.out.println("¡ç--- Receiving Unsuback");
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
			
			while(true) {
				if(receiveDataSize == 0) continue;
				receiveData = new byte[receiveDataSize];
				for(int j=0; j<receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if(c == null) continue;
				if(c.getType() == PacketType.TYPE_SUBACK) {
					c.print();
					System.out.println("¡ç--- Receiving Suback");
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
	
	public void readPuback() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);
			
			System.out.println("*** Waiting Puback Message from Broker...");
			
			while(true) {
				if(receiveDataSize == 0) continue;
				receiveData = new byte[receiveDataSize];
				for(int j=0; j<receiveDataSize; j++)
					receiveData[j] = commBuffer[j];
				Command c = p.parse(receiveData);
				if(c == null) continue;
				if(c.getType() == PacketType.TYPE_PUBACK) {
					c.print();
					System.out.println("¡ç--- Receiving Puback");
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
			System.out.println(" ----¡æ Sending...");
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

	public boolean read() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);

			if (receiveDataSize != 0) {
				receiveData = new byte[receiveDataSize];

				for (int j = 0; j < receiveDataSize; ++j) {
					receiveData[j] = commBuffer[j];
				}
				Command c = p.parse(receiveData);
				if(c != null)
					c.print();
				System.out.println(" ¡ç---- Receiving...");

				StringUtils.printByteArray(receiveData);
				
				System.out.println("");
				System.out.println("");

				if (receiveData[0] == 32)
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
