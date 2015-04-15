package iz.tracex.dto.trac;

/**
 *
 * @author izumikawa_t
 *
 */
public final class UsrTbl {
	private long id;
	private String user_id;
	private String password;
	private String name;
	private String email;
	private boolean receive_mail;
	private int employee_number;
	private int updated_ticket_id;
	private int updated_time;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getpassword() {
		return password;
	}
	public void setpassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isReceive_mail() {
		return receive_mail;
	}
	public void setReceive_mail(boolean receive_mail) {
		this.receive_mail = receive_mail;
	}
	public int getEmployee_number() {
		return employee_number;
	}
	public void setEmployee_number(int employee_number) {
		this.employee_number = employee_number;
	}
	public int getUpdated_ticket_id() {
		return updated_ticket_id;
	}
	public void setUpdated_ticket_id(int updated_ticket_id) {
		this.updated_ticket_id = updated_ticket_id;
	}
	public int getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(int updated_time) {
		this.updated_time = updated_time;
	}
}
