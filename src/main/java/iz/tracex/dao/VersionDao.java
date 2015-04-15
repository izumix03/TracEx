package iz.tracex.dao;

import iz.tracex.dto.trac.Version;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class VersionDao extends AbstractMstDao<String, Version> {

    @Override
    protected Class<Version> entityClass() {
        return Version.class;
    }

    @Override
    public int compare(Version o1, Version o2) {
        return o1.getName().compareTo(o2.getName()) * -1;
    }

    @Override
    protected String getKeyOf(Version t) {
        return t.getName();
    }

    @Override
    protected boolean isMatched(String token, Version t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

}
