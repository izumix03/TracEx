package iz.tracex.dao;

import iz.tracex.dto.trac.M_Function_Group_Evaluator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class M_Function_Group_EvaluatorDao extends AbstractMstDao<Long, M_Function_Group_Evaluator> {

    @Override
    public int compare(M_Function_Group_Evaluator o1, M_Function_Group_Evaluator o2) {
        int result = ObjectUtils.compare(o1.getVersion(), o2.getVersion());
        if (result != 0) {
            return result;
        }
        result = ObjectUtils.compare(o1.getSubsystem(), o2.getSubsystem());
        if (result != 0) {
            return result;
        }
        return ObjectUtils.compare(o1.getFunction_group(), o2.getFunction_group());
    }

    @Override
    protected Long getKeyOf(M_Function_Group_Evaluator t) {
        return t.getId();
    }

    @Override
    protected boolean isMatched(String token, M_Function_Group_Evaluator t) {
        return StringUtils.containsIgnoreCase(t.getFunction_group(), token);
    }

    @Override
    protected Class<M_Function_Group_Evaluator> entityClass() {
        return M_Function_Group_Evaluator.class;
    }

    /**
     * @param version
     * @param subsystem
     * @param function_group
     * @return mst
     */
    public M_Function_Group_Evaluator selectBy(String version, String subsystem, String function_group) {
        // FIXME More quickly!
        for (M_Function_Group_Evaluator t : selectAll()) {
            if (StringUtils.equals(version, t.getVersion()) && StringUtils.equals(subsystem, t.getSubsystem())
                    && StringUtils.equals(function_group, t.getFunction_group())) {
                return t;
            }
        }
        return null;
    }
}
