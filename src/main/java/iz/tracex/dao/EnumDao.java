package iz.tracex.dao;

import iz.tracex.dto.trac.Enum;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class EnumDao extends AbstractMstDao<String, Enum> {

    @Override
    protected Class<Enum> entityClass() {
        return Enum.class;
    }

    @Override
    public int compare(Enum o1, Enum o2) {
        final int result = o1.getType().compareTo(o2.getType());
        if (result != 0) {
            return result;
        }
        return o1.getValue().compareTo(o2.getValue());
    }

    @Override
    protected String getKeyOf(Enum t) {
        return t.getType() + "$" + t.getName();
    }

    @Override
    protected boolean isMatched(String token, Enum t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

    /**
     * @param type
     * @return name list
     */
    public List<String> selectNamesOf(String type) {
        final List<String> result = new ArrayList<>();
        for (Enum e : selectAll()) {
            if (StringUtils.equalsIgnoreCase(type, e.getType())) {
                result.add(e.getName());
            }
        }
        return result;
    }
}
