package command;

import constants.PacketFlag;
import constants.PacketType;

/**
 * PUBLISH COMMAND
 * @author JUNE-HOME
 *
 */
public class PublishCommand extends Command {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		type = PacketType.TYPE_PUBLISH;
		flag = 0; // ���� ���
		remainingLength = 0; // ���� ���
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
