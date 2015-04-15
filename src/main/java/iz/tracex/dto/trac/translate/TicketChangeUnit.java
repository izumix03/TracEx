package iz.tracex.dto.trac.translate;

import iz.tracex.base.TracExUtils;
import iz.tracex.dto.trac.Ticket_Change;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author izumi_j
 *
 */
public final class TicketChangeUnit {
    private long ticket;
    private long time;
    private String author;
    private List<Ticket_Change> changes = new ArrayList<Ticket_Change>();
    
    /**
     * changesの中にcommentがあればリストのインデックスを返す
     * @return
     */
    public int getCommentIndex(){
    	for (int i=0; i<changes.size();i++){
    		if (changes.get(i).getField().equals("comment")){
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * changesの中にcommentがあればコメントを返す
     * @return
     */
    public String getCommentText(){
    	for (int i=0; i<changes.size();i++){
    		if (changes.get(i).getField().equals("comment")){
    			return changes.get(i).getNewvalue();
    		}
    	}
		return null;
    }
   
    
    /**
     * 表示用の時間を返す
     * @return
     */
    public String timeStr() {
        return time > 0 ? TracExUtils.unixTimeToDateTimeStr(time) : "----/--/--";
    }

    /**getter and setter**////////////////////////////////////////////////////////////
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

    public List<Ticket_Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Ticket_Change> changes) {
        this.changes = changes;
    }
    
    public void addChange(Ticket_Change change){
    	this.changes.add(change);
    }
}
