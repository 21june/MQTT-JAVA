package transport;

import java.util.ArrayList;
import java.util.HashMap;

import client.Client;

public class ServerData {
	public static ArrayList<Client> arrClient = new ArrayList<Client>();
	public static ArrayList<Message> arrMessage = new ArrayList<Message>();
	public static HashMap<String, ArrayList<Client>> hashClient = new HashMap<String, ArrayList<Client>>();
	public static HashMap<String, ArrayList<SubscribeMessage>> hashSubMsg = new HashMap<String, ArrayList<SubscribeMessage>>();
	public static HashMap<String, ArrayList<PublishMessage>> hashPubMsg = new HashMap<String, ArrayList<PublishMessage>>();
}