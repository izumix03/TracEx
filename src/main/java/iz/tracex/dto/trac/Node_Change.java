package iz.tracex.dto.trac;

/**
 * 
 * @author izumikawa_t
 * 
 */
public final class Node_Change {
	private long rev;
	private String path;
	private String node_type;
	private String change_type;
	private String base_path;
	private long base_rev;
	
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
	public String getNode_type() {
		return node_type;
	}
	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
	public String getChange_type() {
		return change_type;
	}
	public void setChange_type(String change_type) {
		this.change_type = change_type;
	}
	public String getBase_path() {
		return base_path;
	}
	public void setBase_path(String base_path) {
		this.base_path = base_path;
	}
	public long getBase_rev() {
		return base_rev;
	}
	public void setBase_rev(long base_rev) {
		this.base_rev = base_rev;
	}
	
}
