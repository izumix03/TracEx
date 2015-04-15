package iz.tracex.dto.trac.ini;

import iz.tracex.dto.trac.Ticket;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author izumi_j
 *
 */
public class TicketFields {
	//定義されたOPTION
    public static final Map<String, String[]> OPTIONS;
    static {
        final Map<String, String[]> def = new HashMap<>();
        def.put("resolutions", new String[] { "完了", "対応不要" });
        OPTIONS = Collections.unmodifiableMap(def);
    }
    
    //表示用フィールド
    public static final Map<String, String> FIELDS_MAP;
    static{
    	final Map<String, String> fieldMap = new HashMap<String, String>();
    	
    	Class<?> clazz = DisplayFields.class;
		Field[] ticketCustomFields = clazz.getDeclaredFields();
		
		for (Field f : ticketCustomFields){
				fieldMap.put(f.getName(),FieldTranslater.getJapaneseText(f.getName()));
		}
		
		Class<?> sClazz = Ticket.class;
		Field[] ticketFields = sClazz.getDeclaredFields();
		
		for (Field f : ticketFields){
				fieldMap.put(f.getName(),FieldTranslater.getJapaneseText(f.getName()));
        }
		FIELDS_MAP = Collections.unmodifiableMap(fieldMap);
    }
    
    public static String of(String name) {
		for (String f : FIELDS_MAP.keySet()){
			if (StringUtils.equalsIgnoreCase(f, name)) {
                return f;
            }
        }
        return null;
    }  
    
}
