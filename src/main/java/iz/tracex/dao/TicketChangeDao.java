package iz.tracex.dao;

import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.dto.trac.Ticket_Change;
import iz.tracex.dto.trac.translate.TicketChangeUnit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author izumi_j
 * 
 */
public final class TicketChangeDao extends AbstractDao<Ticket_Change> {
	
	@Override
	protected Class<Ticket_Change> entityClass() {
		return Ticket_Change.class;
	}
	
	/**
	 * @param t
	 */
	public void insert(Ticket_Change t) {
		insert(new String[] { "ticket", "time", "author", "field", "oldvalue", "newvalue" }, new Object[] { t.getTicket(), t.getTime(),
		        t.getAuthor(), t.getField(), t.getOldvalue(), t.getNewvalue() });
	}
	
	/**
	 * @param id
	 * @return oldvalue of field = 'comment'
	 */
	public int selectLatestCommentSeqOf(long id) {
		try {
			final QueryRunner qr = new QueryRunner();
			return qr.query(ConnectionManager.getConnection(),
			        "SELECT oldvalue FROM ticket_change WHERE ticket = ? AND field = 'comment' ORDER BY oldvalue DESC",
			        new ResultSetHandler<Integer>() {
				        @Override
				        public Integer handle(ResultSet rs) throws SQLException {
					        if (rs.next()) {
						        if (NumberUtils.isNumber(rs.getString(1))) {
							        return Integer.parseInt(rs.getString(1));
						        }
						        
					        } else {
						        return 0;
					        }
					        return 0;
				        }
			        }, id);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * @param id
	 * @return changes
	 */
	public List<TicketChangeUnit> selectChangesBy(final long id) {
		try {
			final QueryRunner qr = new QueryRunner();
			return qr.query(ConnectionManager.getConnection(),
			        "SELECT time, author, field, oldvalue, newvalue FROM ticket_change WHERE ticket = ? ORDER BY time DESC",
			        new ResultSetHandler<List<TicketChangeUnit>>() {
				        @Override
				        public List<TicketChangeUnit> handle(ResultSet rs) throws SQLException {
					        long beforeTIme = 0;
					        List<TicketChangeUnit> tcuList = new ArrayList<TicketChangeUnit>();
					        TicketChangeUnit tcu = new TicketChangeUnit();
					        while (rs.next()) {
						        Ticket_Change tc = new Ticket_Change();
						        
						        tc.setTicket(id);
						        tc.setTime(rs.getLong("time"));
						        tc.setAuthor(rs.getString("author"));
						        tc.setField(rs.getString("field"));
						        tc.setOldvalue(rs.getString("oldvalue"));
						        tc.setNewvalue(rs.getString("newvalue"));
						        if (beforeTIme == 0) {
							        tcu.setTicket(tc.getTicket());
							        tcu.setTime(tc.getTime());
							        tcu.setAuthor(tc.getAuthor());
						        }
						        
						        if (beforeTIme != tc.getTime() && beforeTIme != 0) {
							        tcuList.add(tcu);
							        tcu = new TicketChangeUnit();
							        tcu.setTicket(tc.getTicket());
							        tcu.setTime(tc.getTime());
							        tcu.setAuthor(tc.getAuthor());
						        }
						        tcu.addChange(tc);
						        beforeTIme = tc.getTime();
					        }
					        tcuList.add(tcu);
					        return tcuList;
				        }
			        }, id);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public TicketChangeUnit selectChangeUnitBy(final long id) {
		try {
			final QueryRunner qr = new QueryRunner();
			return qr.query(ConnectionManager.getConnection(),
			        "SELECT time, author, field, oldvalue, newvalue FROM ticket_change WHERE ticket = ? ORDER BY time DESC ",
			        new ResultSetHandler<TicketChangeUnit>() {
				        @Override
				        public TicketChangeUnit handle(ResultSet rs) throws SQLException {
					        long beforeTIme = 0;
					        TicketChangeUnit tcu = new TicketChangeUnit();
					        while (rs.next()) {
						        Ticket_Change tc = new Ticket_Change();
						        
						        tc.setTicket(id);
						        tc.setTime(rs.getLong("time"));
						        tc.setAuthor(rs.getString("author"));
						        tc.setField(rs.getString("field"));
						        tc.setOldvalue(rs.getString("oldvalue"));
						        tc.setNewvalue(rs.getString("newvalue"));
						        if (beforeTIme == 0) {
							        tcu.setTicket(tc.getTicket());
							        tcu.setTime(tc.getTime());
							        tcu.setAuthor(tc.getAuthor());
						        }
						        
						        if (beforeTIme != tc.getTime() && beforeTIme != 0) {
							        return tcu;
						        }
						        tcu.addChange(tc);
						        beforeTIme = tc.getTime();
					        }
					        return tcu;
				        }
			        }, id);
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}
}
