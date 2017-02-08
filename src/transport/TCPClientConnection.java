package transport;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import constants.QoS;

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
			System.out.println("");
			System.out.println("[서버로 CONNECT 메시지를 송신합니다.]");
			socket.getOutputStream().write(mergedBytes);
			socket.getOutputStream().flush();
			System.out.println("**********송신 완료**********");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("");
	}

	public boolean read() {
		try {
			int receiveDataSize = 0;
			byte[] commBuffer = new byte[4096];
			byte[] receiveData;
			receiveDataSize = socket.getInputStream().read(commBuffer);
			
			if (receiveDataSize != 0) {
				System.out.println("[서버로부터 메시지를 수신합니다.]");
				receiveData = new byte[receiveDataSize];

				for (int j = 0; j < receiveDataSize; ++j) {
					receiveData[j] = commBuffer[j];
				}
				for (int i = 0; i < receiveData.length; i++) {
					String s1 = String.format("%8s", Integer.toBinaryString(receiveData[i] & 0xFF)).replace(' ', '0');
					System.out.print(s1 + " ");
				}
				System.out.println("");
				System.out.println("**********수신 완료**********");
				if(receiveData[0] == 32)
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
