package iz.tracex.dao;

import iz.tracex.base.TracExConstants;
import iz.tracex.base.WholeCashes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
abstract public class AbstractMstDao<K, T> extends AbstractDao<T> implements Comparator<T> {

    /**
     * @param t
     * @return key
     */
    abstract protected K getKeyOf(final T t);

    /**
     * @param token
     * @param t
     * @return true if matched
     */
    abstract protected boolean isMatched(final String token, final T t);

    /**
     * @param key
     * @return mst
     */
    public T selectBy(final String key) {
        return selectAllMap().get(key);
    }

    /**
     * @param token
     * @return list
     */
    public List<T> selectMatchesBy(final String token) {
        final List<T> result = new ArrayList<>();
        for (T t : selectAll()) {
            if (StringUtils.isEmpty(token) || isMatched(token, t)) {
                result.add(t);
            }
            if (result.size() >= TracExConstants.AUTOCOMPLETE_LIMIT) {
                break;
            }
        }
        return result;
    }

    public List<T> selectAll() {
        final List<T> result = new ArrayList<>(selectAllMap().values());
        Collections.sort(result, this);
        return result;
    }

    private Map<K, T> selectAllMap() {
        final Map<K, T> all = WholeCashes.getCache(getClass());
        if (all.size() == 0) {
            final List<T> tList = query();
            for (T t : tList) {
                all.put(getKeyOf(t), t);
            }
        }
        return all;
    }

}
