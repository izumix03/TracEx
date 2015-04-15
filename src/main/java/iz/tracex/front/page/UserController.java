package iz.tracex.front.page;

import iz.tracex.dao.UsrTblDao;
import iz.tracex.dto.trac.UsrTbl;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.HashMap;
import java.util.Map;

public class UserController implements PageController {
	private final UsrTblDao dao = new UsrTblDao();
//	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Override
	public PageResult process(Map<String, String[]> parameter, String userId) {
		
		// 表示処理
		return handleRetrieve(userId);
	}

	
	private PageResult handleRetrieve(String userId) {
		final Map<String, Object> result = new HashMap<>();
		
		if (userId == null || userId.equals("admin")){
			result.put("notFound", true);
			result.put("title", String.format("不正閲覧(%s)", userId));
		}else{
			UsrTbl usrTbl = dao.getUserTblInfo(userId);
			result.put("usrTbl", usrTbl);
			result.put("title", String.format("(%s)個人ページ", userId));
		}
		
		return PageResult.create(result);
	}
	
}
