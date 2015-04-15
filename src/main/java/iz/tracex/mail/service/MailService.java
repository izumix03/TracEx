package iz.tracex.mail.service;

import iz.tracex.dao.UsrTblDao;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.UsrTbl;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.MailSenderException;
import iz.tracex.mail.base.MailSessionCreator;
import iz.tracex.mail.domain.MailInfo;
import iz.tracex.mail.domain.ReceiverInfo;
import iz.tracex.mail.domain.enumlate.Role;
import iz.tracex.mail.service.generater.state.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
	private final UsrTblDao	userDao	= new UsrTblDao();
	
	/**
	 * メール送信対象者抽出
	 * 
	 * @param t
	 * @param evaluator
	 * @return
	 */
	public List<ReceiverInfo> extractReceiverInfo(Ticket t, String evaluator) {
		// とりあえず置き換え
		String owner = t.getOwner();
		String reporter = t.getReporter();
		List<String> cc = Arrays.asList(t.getCc().split(" "));
		
		// 検索用
		List<String> targetUserId = new ArrayList<String>();
		targetUserId.add(owner);
		targetUserId.add(reporter);
		targetUserId.add(evaluator);
		targetUserId.addAll(cc);
		
		// 検索実行
		List<UsrTbl> userInfo = userDao.getUsrInfoList(targetUserId.toArray(new String[0]));
		
		List<ReceiverInfo> receivers = new ArrayList<ReceiverInfo>();
		// receiverInfo作成
		for (UsrTbl user : userInfo) {
			//メール配信対象かどうか
			if (!user.isReceive_mail()){
				continue;
			}
			
			String userId = user.getUser_id();
			ReceiverInfo receiver = new ReceiverInfo();
			
			receiver.setUserId(userId);
			receiver.setUserName(user.getName());
			receiver.setMailAddress(user.getEmail());
			
			List<Role> roles = new ArrayList<Role>();
			if (userId.equals(owner)) {
				roles.add(Role.DEVELOPER);
			}
			if (userId.equals(reporter)) {
				roles.add(Role.REPORTER);
			}
			if (userId.equals(evaluator)) {
				roles.add(Role.EVALUATOR);
			}
			if (userId.equals(cc)) {
				roles.add(Role.CC);
			}
			receiver.setRoles(roles);
			receivers.add(receiver);
		}
		return receivers;
	}
	
	/**
	 * メールの情報をbuilderを使って作成する
	 * 
	 * @param state
	 * @param receivers
	 * @param ticket
	 * @return
	 * @throws MailSenderException 
	 */
	public List<MailInfo> buildMailInfo(State state, List<ReceiverInfo> receivers, Ticket ticket, TicketCustom tc, TicketChangeUnit change) throws MailSenderException {
		return new MailInfoBuilder(state).execute(receivers, ticket, tc, change);
	}
	
	public void sendMail(List<MailInfo> mailInfos) throws MailSenderException {
		for (MailInfo m : mailInfos) {
			MimeMessage msg = new MimeMessage(MailSessionCreator.createSession());
			String toAddress = m.getReceiver().getMailAddress();
			if (toAddress == null){continue;}
			try {
				msg.setFrom(new InternetAddress("ccm-trac@worksap.co.jp", "CCM Trac", "iso-2022-jp"));
				msg.setSender(new InternetAddress("ccm-trac@worksap.co.jp"));
				
				// TOを設定
				msg.setRecipient(RecipientType.TO, new InternetAddress(toAddress));
				msg.setRecipient(RecipientType.BCC, new InternetAddress("izumikawa_t@worksap.co.jp"));
				
				msg.setSubject(m.getSubject());
				msg.setContent(m.getBody(), "text/plain; charset=ISO-2022-JP");
					
				// メッセージをストリームに書き出すメソッド
				//msg.writeTo(System.out);
				
				Transport.send(msg);
			} catch (MessagingException | IOException e) {
				throw new MailSenderException(e.getMessage());
			}
		}
		
	}
	
}
