package iz.tracex.dao;

import iz.tracex.dto.trac.T_Product_File;

import java.util.List;

/**
 *
 * @author izumi_j
 *
 */
public final class T_ProductFileDao extends AbstractDao<T_Product_File> {

	@Override
	protected Class<T_Product_File> entityClass() {
		return T_Product_File.class;
	}
	
	public List<T_Product_File> selectBy(final long ticket_id) {
		return query(new String[] {"ticket_id"}, new String[]{}, ticket_id);
	}
	
	public List<T_Product_File> selectAll(){
		return query();
	}
}
