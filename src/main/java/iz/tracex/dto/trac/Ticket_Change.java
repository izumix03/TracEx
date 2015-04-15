package iz.tracex.dto.trac;

import iz.tracex.base.TracExUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author izumi_j
 * 
 */
public final class Ticket_Change {
	private long ticket;
	private long time;
	private String author;
	private String field;
	private String oldvalue;
	private String newvalue;
	private String revesion;
	
	private static final Pattern p = Pattern.compile("^\\(In \\[(\\d+)");
	
	public String timeStr() {
		return time > 0 ? TracExUtils.unixTimeToDateTimeStr(time)
				: "----/--/--";
	}

	public boolean isSvnCommitLog() {
		if (newvalue == null){
			return false;
		}
		Matcher m = p.matcher(newvalue);
		return (field.equals("comment") && NumberUtils.isNumber(oldvalue)
				&& m.find());
	}
	
	public String getRevesionOrNull(){
		Matcher m = p.matcher(newvalue);
		if (m.find()){
			setRevision(m.group(1));
			return getRevesion();
		}else{
			return null;
		}
	}
	
	public boolean needTwoCol(){
		if (field.equals("comment") || field.equals("コピー履歴")){
			return true;
		}
		return false;
	}

	public long getTicket() {
		return ticket;
	}

	public void setTicket(long ticket) {
		this.ticket = ticket;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOldvalue() {
		return oldvalue;
	}

	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}

	public String getNewvalue() {
		return newvalue;
	}

	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}
	
	public String getRevesion(){
		return revesion;
	}
	
	public void setRevision(String rev){
		this.revesion = rev;
	}

}
