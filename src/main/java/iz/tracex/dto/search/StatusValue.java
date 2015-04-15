package iz.tracex.dto.search;

import iz.tracex.dto.trac.ini.Status;

public final class StatusValue {
    public Status status;
    public boolean checked;

    public StatusValue(Status status, boolean checked) {
        super();
        this.status = status;
        this.checked = checked;
    }
}
