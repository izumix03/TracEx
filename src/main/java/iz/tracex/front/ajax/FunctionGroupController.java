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

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class FunctionGroupController implements AjaxController<FunctionGroup> {
    private final M_SubsystemDao subsystemDao = new M_SubsystemDao();
    private final M_KinouDao kinouDao = new M_KinouDao();
    private final M_Function_Group_EvaluatorDao functionGrpEvaluatorDao = new M_Function_Group_EvaluatorDao();
    private final UserDao userDao = new UserDao();

    @Override
    public FunctionGroup process(Map<String, String[]> parameter, String userId) {
        // FIXME Verbose code!

        final long pid = parameter.containsKey("pid") ? Long.parseLong(parameter.get("pid")[0]) : 0;
        final String version = parameter.containsKey("version") ? parameter.get("version")[0] : "";
        final String subsystem = parameter.containsKey("subsystem") ? parameter.get("subsystem")[0] : null;
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;

        final M_Subsystem s = subsystemDao.selectBy(pid, subsystem);
        if (s == null) {
            return null;
        }

        final M_Kinou k = kinouDao.selectBy(pid, s.getId(), version, name);
        if (k == null) {
            return null;
        }

        final M_Function_Group_Evaluator f = functionGrpEvaluatorDao.selectBy(version, subsystem, k.getName());

        final FunctionGroup result = new FunctionGroup(s, k, f);
        result.setDevelopper_id(userDao.selectIdByName(result.getDevelopper_name()));

        return result;
    }
}
