package transport;

import java.io.IOException;
import java.net.Socket;

import command.PubackCommand;
import util.ByteUtils;
import util.ParseUtils;

public class TCPClientConnection {

	public Socket socket = null;

	public void start(String serverIP) {
		try {
			socket = new Socket(serverIP, 1883);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] sendData) {
		try {

			ByteUtils.convertBytetoBits(sendData[0]);
			ParseUtils.parse(sendData);
			System.out.println(" ----¡æ Sending...");
			socket.getOutputStream().write(sendData);
			socket.getOutputStream().flush();

			for (int i = 0; i < sendData.length; i++) {
				String s1 = String.format("%8s", Integer.toBinaryString(sendData[i] & 0xFF)).replace(' ', '0');
				System.out.print(s1 + " ");
			}

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
				ParseUtils.parse(receiveData);
				System.out.println(" ¡ç---- Receiving...");
				for (int i = 0; i < receiveData.length; i++) {
					String s1 = String.format("%8s", Integer.toBinaryString(receiveData[i] & 0xFF)).replace(' ', '0');
					System.out.print(s1 + " ");
				}
				
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
