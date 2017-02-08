import java.io.InputStream;

import command.ConnectCommand;
import command.SubscribeCommand;
import constants.PacketFlag;
import constants.PacketType;
import transport.TCPClientConnection;
import util.ByteUtils;

public class main {
	public static void main(String[] args) {
		ConnectCommand c = new ConnectCommand();
		c.init();
		byte[] arr = c.merge();
		for (int i = 0; i < arr.length; i++) {
			String s1 = String.format("%8s", Integer.toBinaryString(arr[i] & 0xFF)).replace(' ', '0');
			System.out.print(s1 + " ");
		}

		TCPClientConnection socket = new TCPClientConnection();
		socket.start("test.mosquitto.org");
		socket.send(arr);

		while (true) {
			if(socket.read())
				break;
		}
		
		SubscribeCommand sc = new SubscribeCommand();
		sc.init();
		byte[] scArr = sc.merge();

		for (int i = 0; i < scArr.length; i++) {
			String s1 = String.format("%8s", Integer.toBinaryString(scArr[i] & 0xFF)).replace(' ', '0');
			System.out.print(s1 + " ");
		}
		socket.send(scArr);
		while (true) {
			if(socket.read())
				break;
		}
	}
}
