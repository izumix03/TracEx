package iz.tracex.front.ajax;

import iz.tracex.dao.M_Function_Group_EvaluatorDao;
import iz.tracex.dao.M_KinouDao;
import iz.tracex.dao.M_SubsystemDao;
import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.M_Function_Group_Evaluator;
import iz.tracex.dto.trac.M_Kinou;
import iz.tracex.dto.trac.M_Subsystem;
import iz.tracex.dto.trac.translate.FunctionGroup;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class FunctionGroupsController implements AjaxController<List<FunctionGroup>> {
    private final M_SubsystemDao subsystemDao = new M_SubsystemDao();
    private final M_KinouDao kinouDao = new M_KinouDao();
    private final M_Function_Group_EvaluatorDao functionGrpEvaluatorDao = new M_Function_Group_EvaluatorDao();
    private final UserDao userDao = new UserDao();

    @Override
    public List<FunctionGroup> process(Map<String, String[]> parameter, String userId) {
        final long pid = parameter.containsKey("pid") ? Long.parseLong(parameter.get("pid")[0]) : 0;
        final String version = parameter.containsKey("version") ? parameter.get("version")[0] : "";
        final String subsystem = parameter.containsKey("subsystem") ? parameter.get("subsystem")[0] : null;
        final String token = parameter.containsKey("query") ? parameter.get("query")[0] : null;

        final M_Subsystem s = subsystemDao.selectBy(pid, subsystem);
        if (s == null) {
            return Collections.emptyList();
        }

        final List<M_Kinou> kList = kinouDao.selectMatchesBy(pid, s.getId(), version, token);
        if (kList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<FunctionGroup> result = new ArrayList<>();
        for (M_Kinou k : kList) {
            final M_Function_Group_Evaluator f = functionGrpEvaluatorDao.selectBy(version, subsystem, k.getName());
            final FunctionGroup fg = new FunctionGroup(s, k, f);
            fg.setDevelopper_id(userDao.selectIdByName(fg.getDevelopper_name()));
            result.add(fg);
        }

        return result;
    }

}
