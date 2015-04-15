package iz.tracex.mail;

import iz.tracex.dao.TicketChangeDao;
import iz.tracex.dao.TicketCustomDao;
import iz.tracex.dao.TicketDao;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.domain.MailInfo;
import iz.tracex.mail.domain.ReceiverInfo;
import iz.tracex.mail.service.MailService;
import iz.tracex.mail.service.generater.state.State;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新規チケット起票時のメール送信(facade) チケット変更時のメール送信を管理する
 * 
 * @author izumikawa_t
 */
public class MailHandler {
	private final MailService	service	= new MailService();
	private static final Logger	logger	= LoggerFactory.getLogger(MailHandler.class);
	
	/**
	 * チケット更新メール送信(非同期処理)
	 * @param t
	 */
	public void sendTicketMail(State state, long ticketId) {		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new RunnableTask(state, ticketId));		
		executor.shutdown();
	}
	
	
	class RunnableTask implements Runnable{
		private final long ticketId;
		private final State state;
		
		RunnableTask(State state, long ticketId){
			super();
			this.ticketId = ticketId;
			this.state = state;
		}
		
		@Override
        public void run() {
			try {
				Ticket ticket = new TicketDao().selectBy(ticketId);
				TicketCustom tc = new TicketCustomDao().selectBy(ticketId);
				TicketChangeUnit change = new TicketChangeDao().selectChangeUnitBy(ticketId);
				
				// メール配信対象者の選別
				List<ReceiverInfo> receivers = service.extractReceiverInfo(ticket, tc.getValue("evaluator"));
				
				// 対象者がいなければ終わり
				if (receivers.isEmpty()) {
					return;
				}
				// メール情報(対象者含む)の組み立て(builder)
				List<MailInfo> mailInfos = service.buildMailInfo(state, receivers, ticket, tc, change);
				
				// コネクションを取得してメール送信
				service.sendMail(mailInfos);
			} catch (MailSenderException e) {
				// 握りつぶす
				logger.debug(e.getMessage());
			}
        }
	}
}
