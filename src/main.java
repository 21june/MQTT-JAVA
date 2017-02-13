import java.io.InputStream;

import command.Command;
import command.ConnectCommand;
import command.PubackCommand;
import command.PublishCommand;
import command.SubscribeCommand;
import constants.PacketFlag;
import constants.PacketType;
import parse.Parse;
import transport.TCPClientConnection;
import util.ByteUtils;
import util.ParseUtils;

public class main {
	public static void main(String[] args) {
		Command c = new ConnectCommand();
		Parse p = new Parse();
		c.init();
		byte[] arr = c.merge();
//		Command tempC = p.parse(arr);
//		tempC.print();
		
		TCPClientConnection socket = new TCPClientConnection();
		socket.start("test.mosquitto.org");
		socket.send(arr);

		while (true) {
			if(socket.read())
				break;
		}
		
		Command sc = new SubscribeCommand();
		sc.init();
		byte[] scArr = sc.merge();
		
		socket.send(scArr);

		while (true) {
			if(socket.read())
				break;
		}

	}
}
