package iz.tracex.front.page.processor;

import iz.tracex.base.TracExUtils;
import iz.tracex.dao.DocDao;
import iz.tracex.dao.TicketCustomDao;
import iz.tracex.dao.TicketDao;
import iz.tracex.dto.trac.Doc;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.ini.Status;
import iz.tracex.dto.trac.translate.TicketCustom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller, Service, Daoパターンは面倒だから、適当に作るよ。
 *
 * @author izumi_j
 *
 */
public final class TicketProcessor{
    private static final Logger logger = LoggerFactory.getLogger(TicketProcessor.class);
    private final static AbsTicketChangeProcessor<Ticket> changeProcessor = new AbsTicketChangeProcessor<Ticket>();
    private TicketProcessor() {
    }

    /**
     * @param t
     * @param tc
     * @param doc
     * @return ticket id
     * @throws TicketOptimisticLockFailedException
     */
    public  static long update(Ticket t,  TicketCustom tc, Ticket tOld,  TicketCustom tcOld, String comment, String userId) throws TicketOptimisticLockFailedException {
        final TicketDao tDao = new TicketDao();
        final TicketCustomDao tcDao = new TicketCustomDao();
        final DocDao dDao = new DocDao();

        if (t.getId() > 0) {
            logger.debug("Update ticket : {}", t.getId());

            // 変更履歴
            if (tOld.getChangetime() > t.getChangetime()) {
                logger.warn("Ticket has already been changed. changed@{} > request@{}", tOld.changetimeStr(),
                        t.changetimeStr());
                throw new TicketOptimisticLockFailedException();
            }
            
            changeProcessor.insertChanges(t, tOld, tc, tcOld, comment, userId, t.getId());

            // 更新
            t.setChangetime(TracExUtils.nowAsUnixTime());
            tDao.update(t);
            tcDao.delete(t.getId());
            tcDao.insert(tc);
        } else {
            logger.debug("Insert new ticket");

            t.setTime(TracExUtils.nowAsUnixTime());
            t.setChangetime(TracExUtils.nowAsUnixTime());
            long newId = tDao.insert(t);
            t.setId(newId);

            tc.setId(newId);
            tcDao.insert(tc);

            // FIXME ちゃんと実装
            final Doc d = new Doc();
            d.setTicket_id(newId);
            dDao.insert(d);
        }

        return t.getId();
    }

    /**
     * チケットコピー
     * @param t
     * @param tc
     * @param userId
     * @return
     * @throws Exception
     */
    public static long copy(Ticket t, TicketCustom tc, String userId)  {
    	 final TicketDao tDao = new TicketDao();
    	 final TicketCustomDao tcDao = new TicketCustomDao();
    	 final DocDao dDao = new DocDao();
    	 final long originId = t.getId();
    	 
    	 t.setTime(TracExUtils.nowAsUnixTime());
         t.setChangetime(TracExUtils.nowAsUnixTime());
         t.setStatus(Status.NEW.toString());
         long newId = tDao.insert(t);
         
         t.setId(newId);
         tc.setId(newId);
         tcDao.insert(tc);
         
         final Doc d = new Doc();         
         d.setTicket_id(newId);
         dDao.insert(d);
         
         AbsTicketChangeProcessor.insertCopyComment(originId, newId, userId);
         
         return t.getId();
    }


}
