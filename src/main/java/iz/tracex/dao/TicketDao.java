package iz.tracex.dao;

import iz.tracex.dto.trac.Ticket;

import java.util.List;

/**
 *
 * @author izumi_j
 *
 */
public final class TicketDao extends AbstractDao<Ticket> {

    @Override
    protected Class<Ticket> entityClass() {
        return Ticket.class;
    }

    /**
     * @param t
     * @return new id
     */
    public long insert(Ticket t) {
        return insert(
                new String[] { "type", "time", "changetime", "component", "severity", "priority", "owner", "reporter",
                        "cc", "version", "milestone", "status", "resolution", "summary", "description",
                "keywords" },
                new Object[] { t.getType(), t.getTime(), t.getChangetime(), t.getComponent(), t.getSeverity(),
                        t.getPriority(), t.getOwner(), t.getReporter(), t.getCc(), t.getVersion(),
                        t.getMilestone(), t.getStatus(), t.getResolution(), t.getSummary(), t.getDescription(),
                        t.getKeywords() });
    }

    /**
     * @param t
     */
    public void update(Ticket t) {
        update(new String[] { "type", "changetime", "component", "severity", "priority", "owner",
                "reporter", "cc", "version", "milestone", "status", "resolution", "summary", "description", "keywords" },
                new Object[] { t.getType(), t.getChangetime(), t.getComponent(),
                t.getSeverity(), t.getPriority(), t.getOwner(), t.getReporter(), t.getCc(), t.getVersion(),
                t.getMilestone(), t.getStatus(), t.getResolution(), t.getSummary(), t.getDescription(),
                t.getKeywords() }, new String[] { "id" }, t.getId());
    }

    /**
     * @param id
     * @return ticket
     */
    public Ticket selectBy(long id) {
        return queryForObject(new String[] { "id" }, id);
    }
    
    /**
     * @return
     */
    public List<Ticket> selectAll(){
    	return query();
    }
    
}
