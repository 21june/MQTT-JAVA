package transport;

import java.io.IOException;
import java.net.Socket;

import command.Command;
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

	public void send(byte[] sendData) {
		try {

			ByteUtils.convertBytetoBits(sendData[0]);

			p.parse(sendData);
			
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
