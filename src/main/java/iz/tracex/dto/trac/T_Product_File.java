package iz.tracex.dto.trac;

/**
 *
 * @author izumi_j
 *
 */
public final class T_Product_File {
    private long id;
    private long ticket_id;
    private long rev;
    private String path;
    private String change_type;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(long ticket_id) {
		this.ticket_id = ticket_id;
	}
	public long getRev() {
		return rev;
	}
	public void setRev(long rev) {
		this.rev = rev;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getChange_type() {
		return change_type;
	}
	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}
    
    
}
