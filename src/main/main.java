package main;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
import transport.TCPServerConnection;
import util.BoolUtils;
import util.ByteUtils;
import util.ParseUtils;
import util.StringUtils;

public class main extends JFrame {
	// zero is normal mode.
	public static int MODE = 0;
	public static Command RECENT_COMMAND = null;
	public static Scanner sc;

	static TCPClientConnection socket = new TCPClientConnection();

	JButton b1 = new JButton("Subscribe");
	JButton b2 = new JButton("Unsubscribe");
	JButton b3 = new JButton("Publish");
	JButton b4 = new JButton("Disconnect");

	main() {
		
		JPanel jp = new JPanel();
		JButton jb1 = new JButton("Client");
		JButton jb2 = new JButton("Broker");
		
		jp.setLayout(new FlowLayout());
		setSize(400, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jp.add(jb1);
		jp.add(jb2);
		
		add(jp);
		
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayClient();
				jp.removeAll();
			}
		});
		
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayBroker();

			}
		});
		
	}

	public static main mf;
	
	public static void main(String[] args) {
		mf = new main();
		sc = new Scanner(System.in);
		/*
		 * Command c = new ConnectCommand(); System.out.print("Broker Address : "); String address = sc.nextLine(); c.init(); byte[] arr = c.merge();
		 */

		// socket.start(address);
		// socket.connect(arr);
		/*
		 * MODE = PacketType.TYPE_CONNECT; Runnable run = new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub while (true) { socket.read(); } } }; new Thread(run).start();
		 */
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

	public static void printOption(TCPClientConnection socket, Scanner s) {
		s = new Scanner(System.in);

		System.out.println("1. Subscribe");
		System.out.println("2. Unsubscribe");
		System.out.println("3. Publish");
		System.out.println("4. Disconnect");

		int i;
		// s.nextInt();
		/*
		 * switch (i) { case 1: subscribe(socket); break; case 2: unsubscribe(socket); break; case 3: publish(socket); break; case 4: disconnect(socket); return; default: break; }
		 */
	}

	static void subscribe(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		SubscribeCommand scom = new SubscribeCommand();
		scom.init();
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<Integer> intArr = new ArrayList<Integer>();

		while (true) {
			String[] items = { "QoS 0", "QoS 1", "QoS 2" };
			JComboBox combo = new JComboBox(items);
			JTextField field1 = new JTextField("a/b");
			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(combo);
			panel.add(new JLabel("Topic Name:"));
			panel.add(field1);
			int result = JOptionPane.showConfirmDialog(null, panel, "Subscribe", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				// SEND
				// System.out.println("Send" + combo.getSelectedItem() + " " + field1.getText());
				String temp_str = field1.getText();
				int temp_qos = combo.getSelectedIndex();
				intArr.add(temp_qos);
				strArr.add(temp_str);
				break;
			} else if (result == JOptionPane.CANCEL_OPTION) {
				// CANCEL
				return;
			} else { // NO
				// NEXT
				System.out.println("NO");
				String temp_str = field1.getText();
				int temp_qos = combo.getSelectedIndex();
				intArr.add(temp_qos);
				strArr.add(temp_str);
			}
			/*
			 * System.out.print("Topic Name(Enter 'X' to exit): "); String tempStr = sc.nextLine(); if (tempStr.equals("X")) break; System.out.print("QoS (0, 1, 2): "); int tempInt
			 * = sc.nextInt(); sc.nextLine(); intArr.add(tempInt); strArr.add(tempStr);
			 */
		}
		int[] qos = new int[intArr.size()];
		String[] str = new String[strArr.size()];
		for (int i = 0; i < strArr.size(); i++) {
			qos[i] = intArr.get(i);
			str[i] = strArr.get(i);
		}
		scom.setCustomTopicFilter(str, qos);

		temp = scom.merge();
		scom.print();
		StringUtils.printByteArray(temp);
		socket.send(temp);
		MODE = PacketType.TYPE_SUBSCRIBE;
		RECENT_COMMAND = scom;
		// socket.read();
		// MODE = 0;
	}

	static void unsubscribe(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		UnsubscribeCommand d = new UnsubscribeCommand();
		d.init();
		ArrayList<String> strArr = new ArrayList<String>();

		while (true) {
			JTextField field1 = new JTextField("a/b");
			JPanel panel = new JPanel(new GridLayout(0, 1));
			panel.add(new JLabel("Topic Name:"));
			panel.add(field1);
			int result = JOptionPane.showConfirmDialog(null, panel, "Unsubscribe", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				String temp_str = field1.getText();
				strArr.add(temp_str);
				break;
			} else if(result == JOptionPane.CANCEL_OPTION) {
				return;
			} else {
				String temp_str = field1.getText();
				strArr.add(temp_str);
			}
			/*
			 * System.out.print("Topic Name(Enter 'X' to exit): "); String tempStr = sc.nextLine(); if (tempStr.equals("X")) break; strArr.add(tempStr);
			 */
		}
		
		String[] str = new String[strArr.size()];
		for (int i = 0; i < strArr.size(); i++) {
			str[i] = strArr.get(i);
		}
		d.setCustomTopicFilter(str);

		temp = d.merge();
		StringUtils.printByteArray(temp);
		d.print();
		socket.send(temp);
		MODE = PacketType.TYPE_UNSUBSCRIBE;
		RECENT_COMMAND = d;
		// MODE = 0;
	}

	static void publish(TCPClientConnection socket) {
		byte[] temp;
		Scanner sc = new Scanner(System.in);
		PublishCommand pc = new PublishCommand();

		pc.init();
				
		String[] items = { "QoS 0", "QoS 1", "QoS 2" };
        JComboBox combo = new JComboBox(items);
        JTextField field1 = new JTextField("a/b");
        JTextField field2 = new JTextField("hello~");
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(combo);
        panel.add(new JLabel("Topic Name:"));
        panel.add(field1);
        panel.add(new JLabel("Message:"));
        panel.add(field2);
        int result = JOptionPane.showConfirmDialog(null, panel, "Publish Message",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
        	pc.setCustomTopicName(field1.getText());
        	pc.setCustomPayload(field2.getText());
        	pc.setQoS(BoolUtils.getBoolQoS((byte) combo.getSelectedIndex()));
        } else {
            System.out.println("Cancelled");
        }
        
/*		System.out.print("Topic Name: ");
		topic = sc.nextLine();
		pc.setCustomTopicName(topic);

		System.out.print("Message: ");
		message = sc.nextLine();
		pc.setCustomPayload(message);

		System.out.print("QoS (0, 1, 2): ");
		int tempInt = sc.nextInt();
		pc.setQoS(BoolUtils.getBoolQoS((byte) tempInt));
*/
        System.out.println("STRING: " + field2.getText());
		temp = pc.merge();

		StringUtils.printByteArray(pc.merge());
		socket.send(temp);
		MODE = PacketType.TYPE_PUBLISH;
		RECENT_COMMAND = pc;
		// if (pc.getQoS() == 1 || pc.getQoS() == 2)
		// socket.read();
		// MODE = 0;
	}

	static void disconnect(TCPClientConnection socket) {
		byte[] temp;
		DisconnectCommand d = new DisconnectCommand();
		d.init();
		temp = d.merge();
		socket.send(temp);
	}

	public void displayClient() {
		JTextField field1 = new JTextField("192.168.0.77");
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Address (Port:1883 TCP):"));
		panel.add(field1);
		int result = JOptionPane.showConfirmDialog(null, panel, "Connect", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			String address = field1.getText();
			socket = new TCPClientConnection();
			socket.start(field1.getText());
			Command c = new ConnectCommand();
			c.init();
			byte[] arr = c.merge();

			socket.connect(arr);

			MODE = PacketType.TYPE_CONNECT;
			RECENT_COMMAND = c;
			Runnable run = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						socket.read();
					}
				}
			};
			new Thread(run).start();

			
		} else {
			System.out.println("Cancelled");
		}
	}
	
	public void displayBroker() {
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Brokering..."));

		Runnable th = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TCPServerConnection server = new TCPServerConnection();
				server.start();
				server.accept();
			}
		};
		
		th.run();
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Broker", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
		} else {
		}
	}
	
	public void setInitUI() {
		setTitle("MQTT Client");
		setSize(500, 100);
		setVisible(true);
		this.setLayout(new FlowLayout());
		this.add(b1);
		this.add(b2);
		this.add(b3);
		this.add(b4);
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				subscribe(socket);
			}
		});

		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				unsubscribe(socket);
			}
		});

		b3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				publish(socket);
			}
		});

		b4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				disconnect(socket);
				System.exit(0);
			}
		});
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void displayPublishPopup(String publishMessage) {
		JTextField field1 = new JTextField(publishMessage);
		JPanel panel = new JPanel(new GridLayout(0, 1));
		panel.add(new JLabel("Contents:"));
		panel.add(field1);
		int result = JOptionPane.showConfirmDialog(null, panel, "Publish", JOptionPane.PLAIN_MESSAGE, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
		} else {
			System.out.println("Cancelled");
		}
	}

}
