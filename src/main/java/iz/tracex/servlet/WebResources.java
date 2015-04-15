package iz.tracex.servlet;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class WebResources {
    private WebResources() {
    }

    /**
     * リソースをキャッシュしてみる。
     */
    private static final Map<String, WebResource> cache = new ConcurrentHashMap<String, WebResource>();

    /**
     * @param resourceName
     * @return resource
     * @throws IOException
     */
    public static WebResource get(String resourceName) throws IOException {
        if (cache.containsKey(resourceName)) {
            return cache.get(resourceName);
        }

        InputStream is = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = new BufferedOutputStream(baos);
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
            if (is == null) {
                throw new FileNotFoundException(resourceName + " not found.");
            }

            int c;

            while ((c = is.read()) != -1) {
                baos.write(c);
            }

            baos.flush();
            byte[] content = baos.toByteArray();

            if (cache.size() > 1000) {// とりあえずMax1,000くらいで
                cache.clear();
            }

            final WebResource result = new WebResource(resourceName, content, parseToContentType(resourceName));

            if (!TracExProperties.getBoolean(Name.DEVELOP)) {
                cache.put(resourceName, result);
            }

            return result;

        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(baos);
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * @param resourceFileName
     * @return content type
     */
    private static String parseToContentType(String resourceFileName) {
        if (resourceFileName.endsWith(".html")) {
            return "text/html";
        }
        if (resourceFileName.endsWith(".js")) {
            return "text/javascript";
        }
        if (resourceFileName.endsWith(".css")) {
            return "text/css";
        }
        if (resourceFileName.endsWith(".jpg")) {
            return "image/jpg";
        }
        if (resourceFileName.endsWith(".png")) {
            return "image/png";
        }
        if (resourceFileName.endsWith(".gif")) {
            return "image/gif";
        }
        if (resourceFileName.endsWith(".ico")) {
            return "image/x-icon";
        }
        return "text/plain";
    }
}
