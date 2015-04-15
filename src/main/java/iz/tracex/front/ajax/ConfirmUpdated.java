package iz.tracex.front.ajax;

import iz.tracex.dao.UsrTblDao;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

public class ConfirmUpdated implements AjaxController<Boolean> {
	private final UsrTblDao userDao = new UsrTblDao();
	
	@Override
	public Boolean process(Map<String, String[]> parameter, String userId) {
		final long id = parameter.containsKey("id") ? Long.parseLong(parameter.get("id")[0]) : 0;
		boolean result = false;
		
		 if (userDao.getUpdatedTicketTime(id, userId) != 0){
	        	if (userDao.getUpdatedTicketTime(id, userId) -  (System.currentTimeMillis() / 1000L) < 10){
	        		result = true;
	        	}
	        	userDao.updateAfterRead(userId);
		 }
		 return result;
}

}
