package constants;
/**
 * Specifies a fixed TYPE's byte.
 * 
 * @author Biocom-June
 *
 */
public class PacketType {
	public static final byte TYPE_RESERVED_0 = 0;
	public static final byte TYPE_CONNECT = 1;
	public static final byte TYPE_CONNACK = 2;
	public static final byte TYPE_PUBLISH = 3;
	public static final byte TYPE_PUBACK = 4;
	public static final byte TYPE_PUBREC = 5;
	public static final byte TYPE_PUBREL = 6;
	public static final byte TYPE_PUBCOMP = 7;
	public static final byte TYPE_SUBSCRIBE = 8;
	public static final byte TYPE_SUBACK = 9;
	public static final byte TYPE_UNSUBSCRIBE = 10;
	public static final byte TYPE_UNSUBACK = 11;
	public static final byte TYPE_PINGREQ = 12;
	public static final byte TYPE_PINGRESP = 13;
	public static final byte TYPE_DISCONNECT = 14;
	public static final byte TYPE_RESERVED_15 = 15;
}
