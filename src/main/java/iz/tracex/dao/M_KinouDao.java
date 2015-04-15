package iz.tracex.dao;

import iz.tracex.dto.trac.M_Kinou;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class M_KinouDao extends AbstractMstDao<String, M_Kinou> {

    @Override
    public int compare(M_Kinou o1, M_Kinou o2) {
        int result = (int) (o1.getPid() - o2.getPid());
        if (result != 0) {
            return result;
        }
        result = (int) (o1.getSid() - o2.getSid());
        if (result != 0) {
            return result;
        }
        return ObjectUtils.compare(o1.getName(), o2.getName());
    }

    @Override
    protected String getKeyOf(M_Kinou t) {
        return t.getPid() + "$" + t.getSid() + "$" + t.getId();
    }

    @Override
    protected boolean isMatched(String token, M_Kinou t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

    @Override
    protected Class<M_Kinou> entityClass() {
        return M_Kinou.class;
    }

    /**
     * @param pid
     * @param sid
     * @param version
     * @param name
     * @return mst
     */
    public M_Kinou selectBy(final long pid, final long sid, final String version, final String name) {
        for (M_Kinou t : selectAll()) {
            if (t.getPid() == pid && t.getSid() == sid && StringUtils.equals(t.getVersion(), version)
                    && StringUtils.equals(t.getName(), name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * @param pid
     * @param sid
     * @param version
     * @param token
     * @return list
     */
    public List<M_Kinou> selectMatchesBy(final long pid, final long sid, final String version, final String token) {
        final List<M_Kinou> result = new ArrayList<>();
        for (M_Kinou t : selectAll()) {
            if (t.getPid() == pid && t.getSid() == sid && StringUtils.equals(t.getVersion(), version)
                    && (StringUtils.isEmpty(token) || isMatched(token, t))) {
                result.add(t);
            }
        }
        return result;
    }

}
