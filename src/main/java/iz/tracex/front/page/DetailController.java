package iz.tracex.front.page;

import iz.tracex.dao.DocDao;
import iz.tracex.dao.TicketChangeDao;
import iz.tracex.dao.TicketCustomDao;
import iz.tracex.dao.TicketDao;
import iz.tracex.dao.UsrTblDao;
import iz.tracex.dto.trac.Doc;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.ini.Status;
import iz.tracex.dto.trac.translate.TicketChangeUnit;
import iz.tracex.dto.trac.translate.TicketCustom;
import iz.tracex.dto.trac.utils.CopyUtil;
import iz.tracex.front.page.helper.DefValueCollector;
import iz.tracex.front.page.helper.DocConverter;
import iz.tracex.front.page.helper.TicketConverter;
import iz.tracex.front.page.processor.DocProcessor;
import iz.tracex.front.page.processor.TicketOptimisticLockFailedException;
import iz.tracex.front.page.processor.TicketProcessor;
import iz.tracex.mail.MailHandler;
import iz.tracex.mail.service.generater.state.ChangeTicketMail;
import iz.tracex.mail.service.generater.state.NewTicketMail;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public class DetailController implements PageController {
    private static final Logger logger = LoggerFactory.getLogger(DetailController.class);

    private static final String ERR_NOT_FOUND = "notFound";
    private static final String ERR_LOCK_FAILED = "lockFailed";

    private final TicketDao ticketDao = new TicketDao();
    private final TicketCustomDao ticketCustomDao = new TicketCustomDao();
    private final TicketChangeDao ticketChangeDao = new TicketChangeDao();
    private final DocDao docDao = new DocDao();
    private final UsrTblDao userDao = new UsrTblDao();

    @Override
    public PageResult process(Map<String, String[]> parameter, String userId) {
        // Ticket id
        final long id = parameter.containsKey("id") ? Long.parseLong(parameter.get("id")[0]) : 0;
        logger.debug("Ticket id = {}", id);

        if (parameter.containsKey("copy-ticket")){
        	// チケットコピー
        	return handleCopy(id, parameter, userId);
        }else if (parameter.containsKey("update-ticket")) {
            // 登録・更新処理(更新はticket単体)
            return handleUpdate(id, parameter, userId);
        } else if(parameter.containsKey("update-doc")) {
            // 登録・更新処理(doc単体)        	
        	return handleDocUpdate(id, parameter, userId);
        } else {
            // 表示処理
            return handleRetrieve(id, parameter, userId);
        }
    }

    /**
     * チケットコピー
     * @param id
     * @param parameter
     * @return result
     */
    private PageResult handleCopy(long id, Map<String, String[]> parameter, String userId) {
        final Map<String, Object> result = new HashMap<>();

        //copy元データ
        final Ticket originTicket = ticketDao.selectBy(id);
        final TicketCustom originTicketCustom = ticketCustomDao.selectBy(id);
                 
        long resultId = 0;
        resultId = TicketProcessor.copy(originTicket, originTicketCustom, userId);
        new MailHandler().sendTicketMail(new NewTicketMail(),resultId);
        
        // リダイレクト
        userDao.updatedBeforeRead(resultId, userId);    
        result.put("id", resultId);
        return PageResult.create("detail", result);
    }
    
    /**
     * チケット更新
     * @param id
     * @param parameter
     * @return result
     */
    private PageResult handleUpdate(long id, Map<String, String[]> parameter, String userId) {
        final Map<String, Object> result = new HashMap<>();
        boolean isNew = false;
        
        //oldデータ
        final Ticket tOld = ticketDao.selectBy(id);
        final TicketCustom tcOld = ticketCustomDao.selectBy(id);
        isNew = tOld == null? true: false;
        
        //新データ：DeepCopy→Convert
        Ticket t = tOld == null ? new Ticket(): (Ticket) CopyUtil.deepCopy(tOld);
        TicketCustom tc =  ticketCustomDao.selectBy(id);
        
        final Pair<Ticket, TicketCustom> ticketSet = TicketConverter.parameterToTicket(parameter, t, tc);
               
        long resultId = 0;
        try {
        	//update
            resultId = TicketProcessor.update(ticketSet.getLeft(), ticketSet.getRight(), tOld, tcOld,
                    parameter.containsKey("comment") ? parameter.get("comment")[0] : null, userId);
            //sendMail
            if (isNew){
            	new MailHandler().sendTicketMail(new NewTicketMail(),resultId);
            }else{
            	new MailHandler().sendTicketMail(new ChangeTicketMail(),resultId);
            }
        } catch (TicketOptimisticLockFailedException e) {
        	//排他制御
            result.putAll(DefValueCollector.collect());
            result.put("statusDefs", Status.availableValuesAsString(Status.find(ticketSet.getLeft().getStatus())));
            result.put("ticket", ticketSet.getLeft());
            result.put("doc", docDao.selectDocBy(id));
            result.put("ticketCustom", ticketSet.getRight());
            result.put(ERR_LOCK_FAILED, true);
            return PageResult.create(result);
        }

        // リダイレクト
        userDao.updatedBeforeRead(resultId, userId);    
        result.put("id", resultId);
        return PageResult.create("detail", result);
    }
    
    /**
     * Doc更新
     * @param id
     * @param parameter
     * @return result
     */
    private PageResult handleDocUpdate(long id, Map<String, String[]> parameter, String userId) {
        final Map<String, Object> result = new HashMap<>();
        
        //oldデータ
        final Doc dOld = docDao.selectDocBy(id);
        final TicketCustom tcOld = ticketCustomDao.selectBy(id);
        
        //新データ：DeepCopy→Convert
        Doc d = (Doc) CopyUtil.deepCopy(dOld);
        TicketCustom tc = ticketCustomDao.selectBy(id);
        final Pair<Doc, TicketCustom> DocSet = DocConverter.parameterToDoc(parameter, d, tc);
        
        long resultId = 0;
        try {
        	//update
            resultId = DocProcessor.update(DocSet.getLeft(), DocSet.getRight(), dOld, tcOld, userId);
            //メール
            new MailHandler().sendTicketMail(new ChangeTicketMail(),resultId);
        } catch (TicketOptimisticLockFailedException e) {
        	//排他制御
            result.putAll(DefValueCollector.collect());
            result.put("ticket", ticketDao.selectBy(id));
            result.put("doc", DocSet.getLeft());
            result.put("ticketCustom", DocSet.getRight());
            result.put(ERR_LOCK_FAILED, true);
            return PageResult.create(result);
        }

        // リダイレクト
        userDao.updatedBeforeRead(resultId, userId);    
        result.put("id", resultId);
        return PageResult.create("detail", result);
    }
    
    /**
     * Ticket&Doc読込
     * @param id
     * @param parameter
     * @return result
     */
    private PageResult handleRetrieve(long id, Map<String, String[]> parameter, String userId) {
        final Map<String, Object> result = new HashMap<>();
        // 定義値など
        result.putAll(DefValueCollector.collect());

        Ticket ticket = null;
        List<TicketChangeUnit> ticketChangeUnit = null;
        Doc doc = null;
        if (id == 0) {
            ticket = new Ticket();
            doc    = new Doc();
        } else {
            ticket = ticketDao.selectBy(id);
            if (ticket == null) {
                ticket = new Ticket();
                ticket.setId(id);
                result.put(ERR_NOT_FOUND, true);
                result.put("title", "チケット登録");
            }else{
            	ticketChangeUnit = ticketChangeDao.selectChangesBy(id);
            	doc = docDao.selectDocBy(id);
            	result.put("title", String.format("%s(%s)", ticket.getId(), ticket.getSummary()));
            }
        }
        result.put("ticket", ticket);
        result.put("statusDefs", Status.availableValuesAsString(Status.find(ticket.getStatus())));
        
        final TicketCustom ticketCustom = ticketCustomDao.selectBy(id);
        result.put("ticketCustom", ticketCustom);
        
        result.put("ticketChangeUnitList", ticketChangeUnit);
        result.put("doc", doc);
        
        return PageResult.create(result);
    }
}
