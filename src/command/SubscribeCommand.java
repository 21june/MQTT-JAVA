package command;

import constants.PacketFlag;
import constants.PacketType;
/**
 * SUBSCRIBE COMMAND
 * @author JUNE-HOME
 *
 */
public class SubscribeCommand extends Command {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_SUBSCRIBE;
		flag = PacketFlag.FLAG_SUBSCRIBE;
		remainingLength = 0; // ¼öÁ¤ ¿ä¸Á
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
