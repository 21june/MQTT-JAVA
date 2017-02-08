package transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import constants.QoS;

public class TCPClientConnection {

	private static Socket socket = null;

	public static void start(String serverIP) {
		try {
			socket = new Socket(serverIP, 5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
