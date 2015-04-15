package iz.tracex.dto.trac.translate;

import iz.tracex.dto.trac.M_Function_Group_Evaluator;
import iz.tracex.dto.trac.M_Kinou;
import iz.tracex.dto.trac.M_Subsystem;

/**
 *
 * @author izumi_j
 *
 */
public final class FunctionGroup {
    private long pid;
    private long sid;
    private String subsystem;
    private String version;
    private String name;

    private String leader_name;
    private String developper_name;
    private String evaluator;

    private String developper_id;

    public FunctionGroup(M_Subsystem s, M_Kinou k, M_Function_Group_Evaluator f) {
        pid = s.getPid();
        sid = k.getSid();
        subsystem = s.getName();
        version = k.getVersion();
        name = k.getName();

        leader_name = k.getLeader_name();
        developper_name = k.getDevelopper_name();
        evaluator = f != null ? f.getEvaluator() : null;
    }

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

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public String getDevelopper_id() {
        return developper_id;
    }

    public void setDevelopper_id(String developper_id) {
        this.developper_id = developper_id;
    }

}
