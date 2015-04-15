package iz.tracex.front.ajax;

import iz.tracex.dao.ClientDao;
import iz.tracex.dto.trac.translate.Client;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

/**
 *
 * @author izumikawa_t
 *
 */
public final class ClientController implements AjaxController<Client> {
    private final ClientDao dao = new ClientDao();

    @Override
    public Client process(Map<String, String[]> parameter, String userId) {
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;
        return dao.selectBy(name);
    }

}
