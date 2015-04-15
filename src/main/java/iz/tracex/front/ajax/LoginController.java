package iz.tracex.front.ajax;

import iz.tracex.dao.UsrTblDao;
import iz.tracex.dto.trac.UsrTbl;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

public final class LoginController implements AjaxController<UsrTbl> {
	private final UsrTblDao dao = new UsrTblDao();
	
	@Override
	public UsrTbl process(Map<String, String[]> parameter, String userId) {
		final String user_id = parameter.containsKey("user_id") ? parameter.get("user_id")[0] : null;
    	final String password = parameter.containsKey("password")? parameter.get("password")[0] : null;
    	
    	UsrTbl usrTbl = dao.getUsrTblBy(user_id, password);
    	
    	if (usrTbl == null){
    		return null;
    	}
		return usrTbl;
	}
}
