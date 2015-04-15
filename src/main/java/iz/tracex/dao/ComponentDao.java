package iz.tracex.dao;

import iz.tracex.dto.trac.Component;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class ComponentDao extends AbstractMstDao<String, Component> {

    @Override
    protected Class<Component> entityClass() {
        return Component.class;
    }

    @Override
    public int compare(Component o1, Component o2) {
        return o1.getName().compareTo(o2.getName());
    }

    @Override
    protected String getKeyOf(Component t) {
        return t.getName();
    }

    @Override
    protected boolean isMatched(String token, Component t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

}
