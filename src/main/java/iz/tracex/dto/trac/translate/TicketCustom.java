package iz.tracex.dto.trac.translate;

import iz.tracex.dto.trac.ini.DisabledTicketCustomFields;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author izumi_j
 *
 */
public final class TicketCustom {
    private long id;
    private final Map<String, String> nameValueMap = new HashMap<>();

    public TicketCustom() {
        for (DisabledTicketCustomFields df : DisabledTicketCustomFields.values()) {
            setValue(df.toString(), df.def());
        }
    }
    
    public TicketCustom(String[] docDisabledColumn){
    	//doc用にセットするものがあれば記載
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue(String name) {
        return nameValueMap.get(name);
    }

    public void setValue(String name, String value) {
        nameValueMap.put(name, value);
    }

    public Set<Entry<String, String>> nameValues() {
        return Collections.unmodifiableSet(nameValueMap.entrySet());
    }
}
