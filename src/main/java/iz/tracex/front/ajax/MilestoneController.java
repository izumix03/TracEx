package iz.tracex.front.ajax;

import iz.tracex.dao.MilestoneDao;
import iz.tracex.dto.trac.Milestone;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class MilestoneController implements AjaxController<Milestone> {
    private final MilestoneDao dao = new MilestoneDao();

    @Override
    public Milestone process(Map<String, String[]> parameter, String userId) {
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;
        return dao.selectBy(name);
    }

}
