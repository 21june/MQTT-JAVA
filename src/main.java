import java.io.InputStream;
import java.util.Scanner;

import command.Command;
import command.ConnectCommand;
import command.DisconnectCommand;
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
		/*
		 * UnsubscribeCommand uc = new UnsubscribeCommand(); uc.init();
		 * 
		 * byte[] arr = uc.merge(); Parse p = new Parse(); UnsubscribeCommand temp = (UnsubscribeCommand) p.parse(arr); temp.print();
		 */
		Scanner s = new Scanner(System.in);
		Command c = new ConnectCommand();
		System.out.print("Broker Address : ");
		String address = s.nextLine();
		Parse p = new Parse();
		c.init();
		byte[] arr = c.merge();

		TCPClientConnection socket = new TCPClientConnection();
		socket.start(address);
		socket.connect(arr);
		socket.readConnack();

		while (true) {
			System.out.println("1. Subscribe");
			System.out.println("2. Unsubscribe");
			System.out.println("3. Publish");
			System.out.println("4. Disconnect");

			int i = s.nextInt();
			byte[] temp;
			switch (i) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				PublishCommand pc = new PublishCommand();
				pc.init();
				temp = pc.merge();
				socket.send(temp);
				break;
			case 4:
				DisconnectCommand d = new DisconnectCommand();
				d.init();
				temp = d.merge();
				socket.send(temp);
				break;
			default:
				break;
			}
		}
		/*
		 * Command c = new ConnectCommand(); Parse p = new Parse(); c.init(); byte[] arr = c.merge();
		 * 
		 * TCPClientConnection socket = new TCPClientConnection(); socket.start("test.mosquitto.org"); socket.send(arr);
		 * 
		 * while (true) { if(socket.read()) break; }
		 * 
		 * Command sc = new SubscribeCommand(); sc.init(); byte[] scArr = sc.merge();
		 * 
		 * socket.send(scArr);
		 * 
		 * while (true) { if(socket.read()) break; }
		 */
	}
}
