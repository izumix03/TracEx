package iz.tracex.dao;

import iz.tracex.dto.trac.M_Product;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class M_ProductDao extends AbstractMstDao<Long, M_Product> {

    @Override
    public int compare(M_Product o1, M_Product o2) {
        return (int) (o1.getId() - o2.getId());
    }

    @Override
    protected Long getKeyOf(M_Product t) {
        return t.getId();
    }

    @Override
    protected boolean isMatched(String token, M_Product t) {
        return StringUtils.containsIgnoreCase(t.getName(), token);
    }

    @Override
    protected Class<M_Product> entityClass() {
        return M_Product.class;
    }

}
