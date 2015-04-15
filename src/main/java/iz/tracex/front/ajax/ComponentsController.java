package iz.tracex.front.ajax;

import iz.tracex.dao.ComponentDao;
import iz.tracex.dto.trac.Component;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class ComponentsController implements AjaxController<List<Component>> {
    private final ComponentDao dao = new ComponentDao();

    @Override
    public List<Component> process(Map<String, String[]> parameter, String userId
    		) {
        final String token = parameter.containsKey("query") ? parameter.get("query")[0] : null;
        return dao.selectMatchesBy(token);
    }

}
