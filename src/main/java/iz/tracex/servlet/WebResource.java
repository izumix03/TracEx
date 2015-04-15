package iz.tracex.servlet;

/**
 *
 * @author izumi_j
 *
 */
public final class WebResource {
    public final String name;
    public final byte[] content;
    public final String contentType;

    public WebResource(String name, byte[] content, String contentType) {
        super();
        this.name = name;
        this.content = content;
        this.contentType = contentType;
    }
}
