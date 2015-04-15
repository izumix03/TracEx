package iz.tracex.base;

import iz.tracex.TracExMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class TracExProperties {
    private static final Logger logger = LoggerFactory.getLogger(TracExMain.class);

    private TracExProperties() {
    }

    public enum Name {
        SERVER("ccm-server"), DATABASE("ccm"), PORT("8081"), USER(null), DEVELOP("false"),//
        ELASTICIP("172.27.91.238"),ELASTICPORT("9300"),  ELASTICCLUSTERNAME("elasticsearch");

        private final String def;

        private Name(String def) {
            this.def = def;
        }

        private static Name find(String name) {
            for (Name n : Name.values()) {
                if (StringUtils.equalsIgnoreCase(n.toString(), name)) {
                    return n;
                }
            }
            return null;
        }
    }

    private static final Map<Name, String> PROP_MAP;
    static {
        PROP_MAP = new ConcurrentHashMap<>();

        File propFile = new File(SystemUtils.getUserDir(), "TracEx.properties");
        if (propFile.exists()) {
            logger.info("Load properties from {}", propFile.getAbsolutePath());
            Properties prop = new Properties();
            try (InputStream is = new FileInputStream(propFile)) {
                prop.load(is);
                for (Map.Entry<Object, Object> entry : prop.entrySet()) {
                    final Name n = Name.find(entry.getKey().toString());
                    if (n == null) {
                        continue;
                    }
                    PROP_MAP.put(n, entry.getValue() != null ? entry.getValue().toString() : null);
                }
            } catch (IOException e) {
                logger.error("Failed to load properties!", e);
            }
        } else {
            logger.warn("{} not found!", propFile.getAbsolutePath());
        }

        for (Name n : Name.values()) {
            if (!PROP_MAP.containsKey(n)) {
                logger.info("Apply default value. {} = {}", n, n.def);
                PROP_MAP.put(n, n.def);
            }
        }
    }

    /**
     * @param propName
     * @return value
     */
    public static String getString(TracExProperties.Name propName) {
        return PROP_MAP.get(propName);
    }

    /**
     * @param propName
     * @return value
     */
    public static int getInt(TracExProperties.Name propName) {
        return Integer.valueOf(PROP_MAP.get(propName));
    }

    /**
     * @param propName
     * @return value
     */
    public static boolean getBoolean(TracExProperties.Name propName) {
        return BooleanUtils.toBoolean(PROP_MAP.get(propName));
    }

}
