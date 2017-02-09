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
		
		socket.send(scArr);
		while (true) {
			if(socket.read())
				break;
		}
	}
}
