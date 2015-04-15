package iz.tracex.front.ajax;

import iz.tracex.dao.M_SubsystemDao;
import iz.tracex.dto.trac.M_Subsystem;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class SubsystemController implements AjaxController<M_Subsystem> {
    private final M_SubsystemDao dao = new M_SubsystemDao();

    @Override
    public M_Subsystem process(Map<String, String[]> parameter, String userId) {
        final long pid = parameter.containsKey("pid") ? Long.parseLong(parameter.get("pid")[0]) : 0;
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;
        return dao.selectBy(pid, name);
    }

}
