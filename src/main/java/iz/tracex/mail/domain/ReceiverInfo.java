package iz.tracex.mail.domain;


import iz.tracex.mail.domain.enumlate.Role;

import java.util.ArrayList;
import java.util.List;

/**
 * メール受信者情報
 * @author izumikawa_t
 */
public class ReceiverInfo {	
	private String	userId;
	private String	userName;
	private String	mailAddress;
	List<Role>	roles	= new ArrayList<Role>();
	
	//getter and setter
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMailAddress() {
		return mailAddress;
	}
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}	
}
