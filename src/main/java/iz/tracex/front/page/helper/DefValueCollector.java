package iz.tracex.front.page.helper;

import iz.tracex.dao.ComponentDao;
import iz.tracex.dao.EnumDao;
import iz.tracex.dao.M_ProductDao;
import iz.tracex.dao.VersionDao;
import iz.tracex.dto.trac.ini.DocFields;
import iz.tracex.dto.trac.ini.TicketCustomFields;
import iz.tracex.dto.trac.ini.TicketFields;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class DefValueCollector {
    private DefValueCollector() {
    }

    public static Map<String, Object> collect() {
        final Map<String, Object> result = new HashMap<>();

        final EnumDao enumDao = new EnumDao();
        final ComponentDao componentDao = new ComponentDao();
        final VersionDao versionDao = new VersionDao();
        final M_ProductDao productDao = new M_ProductDao();

        result.put("severities", enumDao.selectNamesOf("severity"));
        result.put("ticket_types", enumDao.selectNamesOf("ticket_type"));
        result.put("components", componentDao.selectAll());

        result.put("versions", versionDao.selectAll());
        result.put("product_codes", productDao.selectAll());

        result.put("resolutions", TicketFields.OPTIONS.get("resolutions"));

        result.put("occurrence_reasons", TicketCustomFields.OPTIONS.get(TicketCustomFields.OCCURRENCE_REASON));
        result.put("apply_divs", TicketCustomFields.OPTIONS.get(TicketCustomFields.APPLY_DIV));
        result.put("doc_types", DocFields.OPTIONS.get(DocFields.DOC_TYPE));

        return result;
    }
}
