package iz.tracex.dto.trac.translate;

/**
 * Tracでは、session_attributeテーブルのname = 'name'なデータがユーザーです
 *
 * @author izumi_j
 *
 */
public final class User {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
