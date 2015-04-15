package iz.tracex.dao;

import iz.tracex.dto.trac.Milestone;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class MilestoneDao extends AbstractMstDao<String, Milestone> {

    @Override
    protected Class<Milestone> entityClass() {
        return Milestone.class;
    }

    @Override
    public int compare(Milestone o1, Milestone o2) {
        return o1.getName().compareTo(o2.getName());
    }

    @Override
    protected String getKeyOf(Milestone t) {
        return t.getName();
    }

    @Override
    protected boolean isMatched(String token, Milestone t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }
}
