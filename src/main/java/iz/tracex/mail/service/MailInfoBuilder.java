package iz.tracex.mail.service;

import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.MailSenderException;
import iz.tracex.mail.domain.MailInfo;
import iz.tracex.mail.domain.ReceiverInfo;
import iz.tracex.mail.service.generater.state.State;

import java.util.ArrayList;
import java.util.List;

public class MailInfoBuilder {
	private final State state;
	public MailInfoBuilder(State state){
		this.state = state;
	}
	
	private String makeSub(long id, String status, String summary){
		return this.state.makeSub(id, status, summary);
	}
	
	private String makeBody(ReceiverInfo receiver, Ticket ticket, TicketCustom tc, TicketChangeUnit change) throws MailSenderException{
		return this.state.makeBody(receiver, ticket, tc, change);	
	}
	
	public List<MailInfo> execute(List<ReceiverInfo> receivers, Ticket t, TicketCustom tc, TicketChangeUnit change) throws MailSenderException{
		List<MailInfo> infos = new ArrayList<MailInfo>();
		long id = t.getId();
		//件名
		String subject = makeSub(id, t.getStatus(), t.getSummary());
		//レシーバーごとに本文作成
		for (ReceiverInfo r: receivers){
			infos.add(new MailInfo.Builder().receiver(r).subject(subject).body(makeBody(r, t, tc, change)).build());
		}
		return infos;
	}
}
