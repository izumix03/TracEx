package iz.tracex.dto.trac.ini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public enum TicketCustomFields {
    PRODUCT_CODE,
    SUBSYSTEM,
    FUNCTION_GROUP,
    EVALUATOR,
    OCCURRENCE_REASON,
    APPLY_DIV,
    DUE_CLOSE,
    TAG,
    DEVELOPPER_NOTE, 
    AT_SUPORT_TROUBLE_NO,
    CLIENT,
    DOC_TESTER_MEMO,
    DOCTEXT_CHECK,
    DOC_CHECK_MEMO,;

    public static final Map<TicketCustomFields, String[]> OPTIONS;
    static {
        final Map<TicketCustomFields, String[]> def = new HashMap<>();
        def.put(OCCURRENCE_REASON, new String[] { "---", "新規発生", "潜在バグ", "デグレード", "デグレード(AUTOTEST)", "None" });
        def.put(APPLY_DIV, new String[] { "---", "保留願", "対応不要確認" });
        OPTIONS = Collections.unmodifiableMap(def);
    }

    @Override
    public String toString() {
        return StringUtils.lowerCase(name());
    }

    public static TicketCustomFields of(String name) {
        for (TicketCustomFields e : TicketCustomFields.values()) {
            if (StringUtils.equalsIgnoreCase(e.toString(), name)) {
                return e;
            }
        }
        return null;
    }
}
