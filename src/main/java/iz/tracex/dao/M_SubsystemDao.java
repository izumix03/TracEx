package iz.tracex.dao;

import iz.tracex.base.TracExConstants;
import iz.tracex.dto.trac.M_Subsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class M_SubsystemDao extends AbstractMstDao<String, M_Subsystem> {

    @Override
    public int compare(M_Subsystem o1, M_Subsystem o2) {
        int result = (int) (o1.getPid() - o2.getPid());
        if (result != 0) {
            return result;
        } else {
            return ObjectUtils.compare(o1.getName(), o2.getName());
        }
    }

    @Override
    protected String getKeyOf(M_Subsystem t) {
        return t.getPid() + "$" + t.getId();
    }

    @Override
    protected boolean isMatched(String token, M_Subsystem t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

    @Override
    protected Class<M_Subsystem> entityClass() {
        return M_Subsystem.class;
    }

    /**
     * @param pid
     * @param name
     * @return mst
     */
    public M_Subsystem selectBy(final long pid, final String name) {
        for (M_Subsystem t : selectAll()) {
            if (t.getPid() == pid && StringUtils.equals(t.getName(), name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * @param pid
     * @param token
     * @return list
     */
    public List<M_Subsystem> selectMatchesBy(final long pid, final String token) {
        final List<M_Subsystem> result = new ArrayList<>();
        for (M_Subsystem t : selectAll()) {
            if (t.getPid() != pid) {
                continue;
            }

            if (StringUtils.isEmpty(token) || isMatched(token, t)) {
                result.add(t);
            }
            if (result.size() >= TracExConstants.AUTOCOMPLETE_LIMIT) {
                break;
            }
        }
        return result;
    }

}
