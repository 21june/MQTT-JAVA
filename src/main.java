import command.ConnectCommand;
import constants.PacketFlag;
import constants.PacketType;
import util.ByteUtils;

public class main {
    public static void main(String[] args)  {
    	ConnectCommand c = new ConnectCommand();
    	byte[] arr = c.merge();
    	for(int i=0; i<arr.length; i++){
			String s1 = String.format("%8s", Integer.toBinaryString(arr[i] & 0xFF)).replace(' ', '0'); 
			System.out.print(s1 + " ");  
    	}
    }
    
}
 