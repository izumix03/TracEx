package iz.tracex.dto.trac.ini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumikawa_t
 *
 */
public enum DocFields {
	DOC_TYPE;

    public static final Map<DocFields, String[]> OPTIONS;
    static {
        final Map<DocFields, String[]> def = new HashMap<>();
        def.put(DOC_TYPE, new String[] {"", "不具合修正", "機能改善", "新規機能", "法改正対応", "不具合開示" });
        OPTIONS = Collections.unmodifiableMap(def);
    }

    @Override
    public String toString() {
        return StringUtils.lowerCase(name());
    }

    public static DocFields of(String name) {
        for (DocFields e : DocFields.values()) {
            if (StringUtils.equalsIgnoreCase(e.toString(), name)) {
                return e;
            }
        }
        return null;
    }
}
