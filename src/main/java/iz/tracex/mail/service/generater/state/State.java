package iz.tracex.mail.service.generater.state;

import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.mail.MailSenderException;
import iz.tracex.mail.domain.ReceiverInfo;

public interface State {
	String makeSub(long id, String status, String summary) ;
	String makeBody(ReceiverInfo receiver, Ticket ticket, TicketCustom tc,  TicketChangeUnit change) throws MailSenderException;
}
