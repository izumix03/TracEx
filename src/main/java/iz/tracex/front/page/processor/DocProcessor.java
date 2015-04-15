
package iz.tracex.front.page.processor;

import iz.tracex.base.TracExUtils;
import iz.tracex.dao.DocDao;
import iz.tracex.dao.TicketCustomDao;
import iz.tracex.dto.trac.Doc;
import iz.tracex.dto.trac.translate.TicketCustom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller, Service, Daoパターンは面倒だから、適当に作るよ。
 * @author izumikawa_t
 */
public final class DocProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DocProcessor.class);
    private final static AbsTicketChangeProcessor<Doc> changeProcessor = new AbsTicketChangeProcessor<Doc>();

    private DocProcessor() {
    }

    /**
     * @param doc
     * @param tc
     * @return ticket id
     * @throws TicketOptimisticLockFailedException
     */
    public static long update(Doc d,  TicketCustom tc, Doc dOld,  TicketCustom tcOld, String userId) throws TicketOptimisticLockFailedException {
    	final TicketCustomDao tcDao = new TicketCustomDao();
    	final DocDao dDao = new DocDao();
    	
        if (dOld.getChangetime() > d.getChangetime()) {
            logger.warn("Doc has already been changed. changed@{} > request@{}", 
            		dOld.changetimeStr(), d.changetimeStr());
            throw new TicketOptimisticLockFailedException();
        }

        if (d.getTicket_id() > 0) {
            logger.debug("Update doc : {}", d.getTicket_id());
            
            changeProcessor.insertChanges(d, dOld, tc, tcOld, null, userId, d.getTicket_id());
            // 更新
            d.setChangetime(TracExUtils.nowAsUnixTime());
            //updated
            dDao.update(d);
            tcDao.delete(d.getTicket_id());
            tcDao.insert(tc);
        } 
        return d.getTicket_id();
    }

   
}
