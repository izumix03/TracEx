package iz.tracex.dao;

import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.dto.trac.Doc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

/**
 *
 * @author izumi_j
 *
 */
public final class DocDao extends AbstractDao<Doc> {

    @Override
    protected Class<Doc> entityClass() {
        return Doc.class;
    }

    public long insert(Doc t) {
        // FIXME ちゃんと実装
        return insert(new String[] { "ticket_id" }, new Object[] { t.getTicket_id() });
    }
    
    /**
     * @param d
     */
    public void update(Doc t) {
        update(new String[] { "doc_shanai_flg", "doc_devno", "doc_related_ticket", "doc_type", "doc_problem", "doc_solution",
                "doc_exe_file_name", "doc_tekiyou_comment", "doc_tekiyou_junjo", "doc_tekiyou", "doc_fuguai_g", "doc_kakunin_g", "doc_siyou_g" },
                new Object[] { t.getDoc_shanai_flg(), t.getDoc_devno(), t.getDoc_related_ticket(),
                t.getDoc_type(), t.getDoc_problem(), t.getDoc_solution(), t.getDoc_exe_file_name(), t.getDoc_tekiyou_comment(),
                t.getDoc_tekiyou_junjo(), t.getDoc_tekiyou(), t.getDoc_fuguai_g(), t.getDoc_kakunin_g(),t.getDoc_siyou_g()},
                new String[] { "ticket_id" }, t.getTicket_id());
    }
    
    public Doc selectDocBy(long ticket_id){
    	return queryForObject(new String[] { "ticket_id" }, ticket_id);
    }
    
    public List<Long> selectRelatedTickets(long ticket_id){
    	try {
			final QueryRunner qr = new QueryRunner();
			return qr.query(ConnectionManager.getConnection(),
							"SELECT ticket_id FROM doc WHERE doc_related_ticket = ?",
							new ResultSetHandler<List<Long>>() {
								@Override
								public List<Long> handle(ResultSet rs) throws SQLException {
									List<Long> ticketIds = new ArrayList<Long>();		
									while (rs.next()) {
										ticketIds.add(rs.getLong(1));
									} 
									return ticketIds;
								}
							}, ticket_id);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
    }
    
    public List<Doc> selectAll(){
    	return query();
    }
}
