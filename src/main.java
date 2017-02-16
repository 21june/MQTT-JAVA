import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
import util.BoolUtils;
import util.ByteUtils;
import util.ParseUtils;
import util.StringUtils;

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
			switch (i) {
			case 1:
				subscribe(socket);
				break;
			case 2:
				unsubscribe(socket);
				break;
			case 3:
				publish(socket);
				break;
			case 4:
				disconnect(socket);
				return;
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
	
	static void subscribe(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		SubscribeCommand scom = new SubscribeCommand();
		scom.init();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<Integer> intArr = new ArrayList<Integer>();
		
		while(true) {
			System.out.print("Topic Name(Enter 'X' to exit): ");
			String tempStr = sc.nextLine();
			if(tempStr.equals("X"))
				break;
			System.out.print("QoS (0, 1, 2): ");
			int tempInt = sc.nextInt();
			sc.nextLine();
			intArr.add(tempInt);
			strArr.add(tempStr);
		}
		int[] qos = new int[intArr.size()];
		String[] str = new String[strArr.size()];
		for(int i=0; i<strArr.size(); i++) {
			qos[i] = intArr.get(i);
			str[i] = strArr.get(i);
		}
		scom.setCustomTopicFilter(str, qos);

		temp = scom.merge();
		socket.send(temp);
		socket.readSuback();
	}
	
	static void unsubscribe(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		UnsubscribeCommand d = new UnsubscribeCommand();
		d.init();
		ArrayList<String> strArr = new ArrayList<String>();
	
		while(true) {
			System.out.print("Topic Name(Enter 'X' to exit): ");
			String tempStr = sc.nextLine();
			if(tempStr.equals("X"))
				break;
			strArr.add(tempStr);
		}
		String[] str = new String[strArr.size()];
		for(int i=0; i<strArr.size(); i++) {
			str[i] = strArr.get(i);
		}
		d.setCustomTopicFilter(str);

		temp = d.merge();
		socket.send(temp);
		socket.readUnsuback();
		
	}
	
	static void publish(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		PublishCommand pc = new PublishCommand();
		String topic;
		String message;
		
		pc.init();
		System.out.print("Topic Name: ");
		topic = sc.nextLine();
		pc.setCustomTopicName(topic);
		
		System.out.print("Message: ");
		message = sc.nextLine();
		pc.setCustomPayload(message);

		System.out.print("QoS (0, 1, 2): ");
		int tempInt = sc.nextInt();
		pc.setQoS(BoolUtils.getBoolQoS((byte)tempInt));
		
		temp = pc.merge();
		socket.send(temp);
		if(pc.getQoS() == 1 || pc.getQoS() == 2)
			socket.readPuback();
	}
	
	static void disconnect(TCPClientConnection socket) {
		byte[] temp;
		DisconnectCommand d = new DisconnectCommand();
		d.init();
		temp = d.merge();
		socket.send(temp);
	}
}
