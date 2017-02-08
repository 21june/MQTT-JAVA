package constants;

/**
 * Specifies a fixed FLAG value except the PUBLISH message type.
 * 
 * @author Biocom-June
 *
 */
public class PacketFlag {
	public static byte FLAG_CONNECT = 0x00;
	public static byte FLAG_CONNACK = 0x00;
	// public static byte FLAG_PUBLISH; //Bit3: DUP, Bit2: QoS, Bit1: QoS, Bit0:RETAIN
	public static byte FLAG_PUBACK = 0x00;
	public static byte FLAG_PUBREC = 0x00;
	public static byte FLAG_PUBREL = 0x02;
	public static byte FLAG_PUBCOMP = 0x00;
	public static byte FLAG_SUBSCRIBE = 0x02;
	public static byte FLAG_SUBACK = 0x00;
	public static byte FLAG_UNSUBSCRIBE = 0x02;
	public static byte FLAG_UNSUBACK = 0x00;
	public static byte FLAG_PINGREQ = 0x00;
	public static byte FLAG_PINGRESP = 0x00;
	public static byte FLAG_DISCONNECT = 0x00;
}
