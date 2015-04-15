package iz.tracex.dto.trac;

/**
 *
 * @author izumi_j
 *
 */
public final class M_Subsystem {
    private long pid;
    private long id;
    private String name;
    private String from_version;
    private String to_version;
    private String block;
    private String manager_name;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom_version() {
        return from_version;
    }

    public void setFrom_version(String from_version) {
        this.from_version = from_version;
    }

    public String getTo_version() {
        return to_version;
    }

    public void setTo_version(String to_version) {
        this.to_version = to_version;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getManager_name() {
        return manager_name;
    }

    public void setManager_name(String manager_name) {
        this.manager_name = manager_name;
    }

}
