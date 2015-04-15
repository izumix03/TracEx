package iz.tracex.dao;

import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.dto.trac.translate.TicketCustom;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

/**
 *
 * @author izumi_j
 *
 */
public final class TicketCustomDao extends AbstractDao<TicketCustom> {

    @Override
    protected Class<TicketCustom> entityClass() {
        return TicketCustom.class;
    }

    /**
     * @return tableName
     */
    @Override
    protected String tableName() {
        return "ticket_custom";
    }

    /**
     * @param tc
     */
    public void insert(TicketCustom tc) {
        for (Map.Entry<String, String> nameValue : tc.nameValues()) {
            insert(new String[] { "ticket", "name", "value" },
                    new Object[] { tc.getId(), nameValue.getKey(), nameValue.getValue() });
        }
    }
    
    /**
     * @param id
     */
    public void delete(long id) {
        delete(new String[] { "ticket" }, id);
    }
    
    /**
     * @param id
     * @param name
     */
    public void deleteByName(long id, String[] names){
    	Object[] args = new Object[names.length+1]; 
    	args[0] = id;
    	
    	StringBuilder sb = new StringBuilder();
    	sb.append("DELETE FROM TICKET_CUSTOM WHERE TICKET=? AND NAME IN (");
    	for (int i = 0; i < names.length; i++){
    		if (i == 0){
    			sb.append("?");
    		}else{
    			sb.append(",?");
    		}
    		args[i+1] = names[i];
    	}
    	sb.append(");");
    	
    	
    	try{
    		final QueryRunner qr = new QueryRunner();
    		qr.update(ConnectionManager.getConnection(), sb.toString(), args);
    	} catch (SQLException e){
    		throw new IllegalStateException(e);
    	}
    }
    
        

    /**
     * @param id
     * @return dto
     */
    public TicketCustom selectBy(long id) {
        final TicketCustom result = new TicketCustom();
        result.setId(id);

        final List<Map<String, Object>> source = queryMap(new String[] { "ticket" }, new String[] {}, id);
        for (Map<String, Object> row : source) {
            final Object name = row.get("name");
            final Object value = row.get("value");
            if (name != null) {
                result.setValue(name.toString(), value != null ? value.toString() : null);
            }
        }

        return result;
    }

}
