package iz.tracex.dto.trac;

/**
 *
 * @author izumi_j
 *
 */
public final class M_Kinou {
    private long pid;
    private long sid;
    private long id;
    private String name;
    private String version;
    private String leader_name;
    private String developper_name;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getDevelopper_name() {
        return developper_name;
    }

    public void setDevelopper_name(String developper_name) {
        this.developper_name = developper_name;
    }

}
