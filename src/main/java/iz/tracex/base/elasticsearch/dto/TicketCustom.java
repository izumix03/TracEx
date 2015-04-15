package iz.tracex.base.elasticsearch.dto;


/**
 *
 * @author izumikawa_t
 * elastic search用
 * idは一意キーにする、ticketidで処理する、必要カラムだけに絞る
 */
public final class TicketCustom {
    private long ticketId;
    private String name;
    private String value;
    
	public long getTicketId() {
		return ticketId;
	}
	public void setTicketId(long ticketId) {
		this.ticketId = ticketId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
    
    
}
