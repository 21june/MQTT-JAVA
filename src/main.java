import command.ConnectCommand;
import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

public class main {
    public static void main(String[] args)  {
    	byte connect = PacketType.TYPE_CONNECT;
    	byte flag = PacketFlag.FLAG_CONNECT;
    	byte remainingLength = 0;
    	byte msbLengthforProtocolName = 0;
    	byte lsbLengthforProtocolName = 4;
    	byte[] protocolName = new byte[msbLengthforProtocolName + lsbLengthforProtocolName];
    	protocolName[0] = 'M';
    	protocolName[1] = 'Q';
    	protocolName[2] = 'T';
    	protocolName[3] = 'T';
    	byte protocolLevel = 4;
    	byte connectFlag = 0;
    	byte msbKeepAlive = 0;
    	byte lsbKeepAlive = 10;
    	byte clientIdentifier = 0;
    	
    }
    
}
 