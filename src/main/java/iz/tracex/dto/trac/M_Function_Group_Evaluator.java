package iz.tracex.dto.trac;

/**
 *
 * @author izumi_j
 *
 */
public final class M_Function_Group_Evaluator {
    private long id;
    private String product_code;
    private String version;
    private String subsystem;
    private String function_group;
    private String evaluator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }

    public String getFunction_group() {
        return function_group;
    }

    public void setFunction_group(String function_group) {
        this.function_group = function_group;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

}
