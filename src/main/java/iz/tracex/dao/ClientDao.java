package iz.tracex.dao;

import iz.tracex.dto.trac.translate.Client;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumikawa_t
 *
 */
public final class ClientDao extends AbstractMstDao<String, Client> {

	@Override
	public int compare(Client o1, Client o2) {
		return 0;
	}

	@Override
	protected String getKeyOf(Client c) {
		return c.getId();
	}

	@Override
	protected boolean isMatched(String token, Client c) {
		return StringUtils.containsIgnoreCase(c.getId(), token);
	}

	@Override
	protected Class<Client> entityClass() {
		 return Client.class;
	}

	  @Override
	    protected String tableName() {
	        return "(SELECT  value as id FROM ticket_custom WHERE  name='client' and value!='' and value is not null group by value) client";
	    }

	    @Override
	    protected String buildSelectBase() {
	        return safeJoin("SELECT * FROM", tableName());
	    }

	    /**
	     * @param name
	     * @return id
	     */
	    public String selectIdByName(String name) {
	        for (Client u : selectAll()) {
	            if (StringUtils.equals(name, u.getId())) {
	                return u.getId();
	            }
	        }
	        return null;
	    }

}
