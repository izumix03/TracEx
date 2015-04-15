package iz.tracex.dto.search;


/**
 *
 * @author izumi_j
 *
 */
public final class SortValue {
    public String field;
    public boolean desc;

    public SortValue(String field, boolean desc) {
        super();
        this.field = field;
        this.desc = desc;
    }
}
