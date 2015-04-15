package iz.tracex.mail.service.generater.state;

import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.MailSenderException;
import iz.tracex.mail.domain.ReceiverInfo;
import iz.tracex.mail.domain.enumlate.Role;
import iz.tracex.mail.service.generater.VelocityWrapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeTicketMail implements State {
	private static final Logger	logger	= LoggerFactory.getLogger(ChangeTicketMail.class);
	
	@Override
	public String makeSub(long id, String status, String summary) {
		return String.format("【CHANGE!!】%d(%s)「%s」", id, status, summary);
	}
	
	@Override
	public String makeBody(ReceiverInfo receiver, Ticket ticket, TicketCustom tc,  TicketChangeUnit change) throws MailSenderException {
		VelocityWrapper vel = null;
		try {
			// メールの本文を指定			
			vel = new VelocityWrapper("ChangeTIcketMail.vm");
			//「泉川」または「泉川　貴洋」
			vel.put("userName", receiver.getUserName().split(" ")[0] == null ? receiver.getUserName() : receiver.getUserName().split(" ")[0]);
			//「開発者、報告者、評価者、CC」
			vel.put("role", createRole(receiver.getRoles()));						
			vel.put("ticket", ticket);
			vel.put("change" ,change);
			return vel.merge();
		} catch (Exception e) {
			logger.error(String.format("receiver:%s & ticket:%s", receiver, ticket));
			throw new iz.tracex.mail.MailSenderException(e.getMessage());
		}
	}

	/**
	 * 役割を表示用に組み立てる
	 * 
	 * @param roles
	 * @return
	 */
	private Object createRole(List<Role> roles) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < roles.size(); i++) {
			if (i == 0) {
				sb.append(roles.get(i).getRole());
			} else {
				sb.append("、").append(roles.get(i).getRole());
			}
		}
		return sb.toString();
	}
	
}
