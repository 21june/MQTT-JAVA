package command;

import constants.PacketFlag;
import constants.PacketType;
/**
 * SUBACK COMMAND
 * @author JUNE-HOME
 *
 */
public class SubackCommand extends Command {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_SUBACK;
		flag = PacketFlag.FLAG_SUBACK;
		remainingLength = new byte[]{0}; // ¼öÁ¤ ¿ä¸Á
	}

	@Override
	public byte[] merge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parse(byte[] bytes) {
		// TODO Auto-generated method stub
		
	}

}
