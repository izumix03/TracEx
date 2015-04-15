package iz.tracex.front.ajax;

import iz.tracex.dao.ComponentDao;
import iz.tracex.dto.trac.Component;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class ComponentController implements AjaxController<Component> {
    private final ComponentDao dao = new ComponentDao();

    @Override
    public Component process(Map<String, String[]> parameter, String userId) {
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;
        return dao.selectBy(name);
    }

}
