package iz.tracex.base.elasticsearch.dao;

import iz.tracex.base.elasticsearch.dto.TicketCustom;
import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.dao.AbstractDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 *
 * @author izumikawa_t
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
     * 存在するticketIdかつ、null、空文字除外のリスト
     * @return
     */
    public List<TicketCustom> selectAll(){    	
    	List<TicketCustom> results = new ArrayList<TicketCustom>();	  
        final List<Map<String, Object>> source = queryMap("SELECT * FROM ticket_custom where value != '' and value is not null and ticket in (select id from ticket)");        
        for (Map<String, Object> row : source) {
        	final TicketCustom result = new TicketCustom();            
            result.setTicketId(  Long.parseLong(row.get("ticket").toString()));
            result.setName((String) row.get("name"));
            result.setValue((String) row.get("value"));            
            results.add(result);            
        }
        return results;
    }

    
	public int getCount()  {
		final QueryRunner qr = new QueryRunner();
		try {
	        return qr.query(ConnectionManager.getConnection(), "select count(*) as cnt from ticket_custom where value != '' and value is not null and ticket in (select id from ticket) ", new ResultSetHandler<Integer>(){
	        	@Override
	            public Integer handle(ResultSet rs) throws SQLException {
	        		while (rs.next()){
	        			return rs.getInt("cnt");
	        			
	        		}
					return 0;
	            }
	        }) ;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
	
    }

	public List<TicketCustom> selectLimit(int offsetCnt, int limitCnt) {
		List<TicketCustom> results = new ArrayList<TicketCustom>();	  
        final List<Map<String, Object>> source = queryMap("SELECT * FROM ticket_custom where value != '' and value is not null and ticket in (select id from ticket) order by 1, 2  limit ?, ?", offsetCnt, limitCnt);        
        for (Map<String, Object> row : source) {
        	final TicketCustom result = new TicketCustom();            
            result.setTicketId(  Long.parseLong(row.get("ticket").toString()));
            result.setName((String) row.get("name"));
            result.setValue((String) row.get("value"));            
            results.add(result);            
        }
        return results;
    }

}
