package iz.tracex.mail.service.generater.state;

import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.MailSenderException;
import iz.tracex.mail.domain.ReceiverInfo;
import iz.tracex.mail.domain.enumlate.Role;
import iz.tracex.mail.service.generater.VelocityWrapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewTicketMail implements State {
	private static final Logger	logger	= LoggerFactory.getLogger(NewTicketMail.class);
	private final UserDao dao = new UserDao();
	
	@Override
	public String makeSub(long id, String status, String summary) {
		return String.format("【NEW!!】%d「%s」", id, summary);
	}
	
	@Override
	public String makeBody(ReceiverInfo receiver, Ticket ticket, TicketCustom tc, TicketChangeUnit change) throws MailSenderException {
		VelocityWrapper vel = null;
		try {
			// メールの本文を指定			
			vel = new VelocityWrapper("NewTIcketMail.vm");
			//「泉川」または「泉川　貴洋」
			vel.put("userName", receiver.getUserName().split(" ")[0] == null ? receiver.getUserName() : receiver.getUserName().split(" ")[0]);
			//「開発者、報告者、評価者、CC」
			vel.put("role", createRole(receiver.getRoles()));
			//「デリバティブ管理_デリバティブ管理設定_デリバティブ共通設定-デリバティブタイプ設定」
			vel.put("subsystem", tc.getValue("subsystem"));
			vel.put("function", tc.getValue("function_group"));
			
			vel.put("reporter", dao.selectBy(ticket.getReporter()).getName());
			vel.put("owner", dao.selectBy(ticket.getOwner()).getName());
			vel.put("evaluator", dao.selectBy(tc.getValue("evaluator")).getName());
			vel.put("cc", createCc(ticket.getCc()));
			
			vel.put("ticket", ticket);
			return vel.merge();
		} catch (Exception e) {
			logger.error(String.format("receiver:%s & ticket:%s", receiver, ticket));
			throw new MailSenderException(e.getMessage());
		}
	}
	
	/**
	 * CCを表示用に組み立てる
	 * @param name
	 * @return
	 */
	private Object createCc(String name) {
		String[] ccs = name.split(" ");
		List<String> ccNames = new ArrayList<String>();
		
		for (String cc: ccs){
			ccNames.add(dao.selectBy(cc).getName());
		}
		
	    return StringUtils.join(ccNames, ",");
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