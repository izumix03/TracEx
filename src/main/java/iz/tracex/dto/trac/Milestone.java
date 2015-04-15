package iz.tracex.dto.trac;

import iz.tracex.base.TracExUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class Milestone {

    public String dueStr() {
        return TracExUtils.unixTimeToDateTimeStr(due);
    }

    public String completedStr() {
        return TracExUtils.unixTimeToDateTimeStr(completed);
    }
    
    private String id;
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String name;
    private long due;
    private long completed;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDue() {
        return due;
    }

    public void setDue(long due) {
        this.due = due;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
