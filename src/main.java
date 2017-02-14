import java.io.InputStream;

import command.Command;
import command.ConnectCommand;
import command.PubackCommand;
import command.PublishCommand;
import command.SubackCommand;
import command.SubscribeCommand;
import command.UnsubscribeCommand;
import constants.PacketFlag;
import constants.PacketType;
import parse.Parse;
import transport.TCPClientConnection;
import util.ByteUtils;
import util.ParseUtils;

public class main {
	public static void main(String[] args) {
		
		UnsubscribeCommand uc = new UnsubscribeCommand();
		uc.init();

		byte[] arr = uc.merge();
		Parse p = new Parse();
		UnsubscribeCommand temp = (UnsubscribeCommand) p.parse(arr);
		temp.print();
		/*
		Command c = new ConnectCommand();
		Parse p = new Parse();
		c.init();
		byte[] arr = c.merge();
		
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
		*/
	}
}
