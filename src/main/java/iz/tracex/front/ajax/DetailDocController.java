package iz.tracex.front.ajax;

import iz.tracex.dao.DocDao;
import iz.tracex.dao.T_ProductFileDao;
import iz.tracex.dto.trac.T_Product_File;
import iz.tracex.dto.trac.translate.DetailDoc;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DetailDocController implements AjaxController<DetailDoc> {
	private final T_ProductFileDao refsDao = new T_ProductFileDao();
	private final DocDao docDao = new DocDao();
		
	@Override
	public DetailDoc process(Map<String, String[]> parameter, String userId) {
		DetailDoc detailDoc = new DetailDoc();
		
		final long ticket_id= parameter.containsKey("ticket_id") ? Long.parseLong(parameter.get("ticket_id")[0]) : 0;
		List<T_Product_File> files = refsDao.selectBy(ticket_id);
		
		//sqlでgroupingしても良いけど。。。
		Set<String> fileNames = new HashSet<String>();
		for (T_Product_File f :files){
			fileNames.add(f.getPath());
		}
		
		detailDoc.setRefs(new ArrayList<String>(fileNames));
		detailDoc.setTicketList(docDao.selectRelatedTickets(ticket_id));
		
		return detailDoc;
	}
	
}
