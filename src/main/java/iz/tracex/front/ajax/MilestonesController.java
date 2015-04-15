package iz.tracex.front.ajax;

import iz.tracex.dao.MilestoneDao;
import iz.tracex.dto.trac.Milestone;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class MilestonesController implements AjaxController<List<Milestone>> {
    private final MilestoneDao dao = new MilestoneDao();

    @Override
    public List<Milestone> process(Map<String, String[]> parameter, String userId) {
        final String nameToken = parameter.containsKey("query") ? parameter.get("query")[0] : null;
        List<Milestone> milestones =  dao.selectMatchesBy(nameToken);
        for (Milestone m : milestones){
        	m.setId(m.getName());
        }
        return milestones;
    }

}
