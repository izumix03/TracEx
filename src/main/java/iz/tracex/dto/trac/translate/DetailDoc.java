package iz.tracex.dto.trac.translate;

import java.util.List;

/**
 * detail-docページのajax通信用データ構造体、、、あまり良くない
 *
 * @author izumikawa_t
 *
 */
public final class DetailDoc {
    private List<String> refs;
    private List<Long> ticketList;
    
	public List<String> getRefs() {
		return refs;
	}
	public void setRefs(List<String> refs) {
		this.refs = refs;
	}
	public List<Long> getTicketList() {
		return ticketList;
	}
	public void setTicketList(List<Long> ticketList) {
		this.ticketList = ticketList;
	}
}
