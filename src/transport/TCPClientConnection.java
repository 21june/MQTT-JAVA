package transport;

import java.io.IOException;
import java.net.Socket;

public class TCPClientConnection {

	public Socket socket = null;

	public void start(String serverIP) {
		try {
			socket = new Socket(serverIP, 1883);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(byte[] mergedBytes) {
		try {
			socket.getOutputStream().write(mergedBytes);
			socket.getOutputStream().flush();
			System.out.println("[서버로 CONNECT 데이터를 송신했습니다.]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void read() {
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
				for (int i = 0; i < receiveData.length; i++) {
					String s1 = String.format("%8s", Integer.toBinaryString(receiveData[i] & 0xFF)).replace(' ', '0');
					System.out.print(s1 + " ");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
