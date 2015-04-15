package iz.tracex.dao;

import iz.tracex.dto.trac.translate.User;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class UserDao extends AbstractMstDao<String, User> {

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

    @Override
    protected String tableName() {
        return "(SELECT sid AS id, value AS name FROM session_attribute WHERE name = 'name') user";
    }

    @Override
    protected String buildSelectBase() {
        return safeJoin("SELECT * FROM", tableName());
    }

    @Override
    public int compare(User o1, User o2) {
        return o1.getId().compareTo(o2.getId());
    }

    @Override
    protected String getKeyOf(User t) {
        return t.getId();
    }

    @Override
    protected boolean isMatched(String token, User t) {
        return StringUtils.startsWithIgnoreCase(t.getId(), token)
                || StringUtils.startsWithIgnoreCase(t.getName(), token);
    }

    /**
     * @param name
     * @return id
     */
    public String selectIdByName(String name) {
        for (User u : selectAll()) {
            if (StringUtils.equals(name, u.getName())) {
                return u.getId();
            }
        }
        return null;
    }

}
