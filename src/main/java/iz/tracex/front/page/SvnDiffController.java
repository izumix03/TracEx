package iz.tracex.front.page;

import iz.tracex.dao.NodeChangeDao;
import iz.tracex.dao.TicketDao;
import iz.tracex.dto.trac.Node_Change;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * コード差分表示
 * @author izumikawa_t
 */
public class SvnDiffController implements PageController {
	private static final Logger logger = LoggerFactory.getLogger(SvnDiffController.class);

	private final NodeChangeDao nodeChangeDao = new NodeChangeDao();
	private final TicketDao ticketDao = new TicketDao();

	@Override
	public PageResult process(Map<String, String[]> parameter, String userId) {

		final long revesion = parameter.containsKey("rev") ? Long.parseLong(parameter.get("rev")[0]) : 0;
		final long id = parameter.containsKey("id") ? Long.parseLong(parameter.get("id")[0]) : 0;
		logger.debug("revesion = {}", revesion);
		// 表示処理
		return handleRetrieve(id, revesion);
	}

	
	private PageResult handleRetrieve(long id, long rev) {
		final Map<String, Object> result = new HashMap<>();
		List<Node_Change> nodeChangeList = nodeChangeDao.selectChangesBy(rev);
		Ticket ticket = ticketDao.selectBy(id);
		result.put("ticket", ticket);
		result.put("nodeChangeList", nodeChangeList);
		result.put("rev", rev);
		result.put("title", String.format("(%s)チェンジセット", rev));
		return PageResult.create(result);
	}
	
}
