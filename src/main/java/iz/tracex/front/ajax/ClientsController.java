package iz.tracex.front.ajax;

import iz.tracex.dao.ClientDao;
import iz.tracex.dto.trac.translate.Client;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.List;
import java.util.Map;

public final class ClientsController implements AjaxController<List<Client>> {
	 private final ClientDao dao = new ClientDao();

	    @Override
	    public List<Client> process(Map<String, String[]> parameter, String userId) {
	        final String nameToken = parameter.containsKey("query") ? parameter.get("query")[0] : null;
	        List<Client> clients =  dao.selectMatchesBy(nameToken);
	        
	        return clients;
	    }
}
