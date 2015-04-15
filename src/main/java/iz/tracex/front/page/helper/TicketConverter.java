package iz.tracex.front.page.helper;

import iz.tracex.base.TracExUtils;
import iz.tracex.dto.search.SearchResult;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.ini.DisplayFields;
import iz.tracex.dto.trac.ini.Status;
import iz.tracex.dto.trac.ini.TicketCustomFields;
import iz.tracex.dto.trac.translate.TicketCustom;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author izumi_j
 *
 */
public final class TicketConverter {
    private TicketConverter() {
    }

    public static List<SearchResult> ticketsToMaps(List<DisplayFields> items, String groupedBy) {
        final List<SearchResult> results = new ArrayList<>();

        String wkGrp = null;
        List<Map<String, String>> list = new ArrayList<>();
        for (DisplayFields t : items) {
            try {
                final Map<String, String> tMap = BeanUtils.describe(t);
                tMap.put("time", TracExUtils.unixTimeToDateTimeStr(t.getTime()));
                tMap.put("changetime", TracExUtils.unixTimeToDateTimeStr(t.getChangetime()));

                final String curGrp = StringUtils.isNotEmpty(groupedBy) ? tMap.get(groupedBy) : SearchResult.NON_GRP;

                if (wkGrp != null && !StringUtils.equals(wkGrp, curGrp)) {
                    results.add(new SearchResult(wkGrp, list));
                    list = new ArrayList<>();
                }

                list.add(tMap);
                wkGrp = curGrp;

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException(e);
            }
        }
        if (!list.isEmpty()) {
            results.add(new SearchResult(wkGrp, list));
        }

        return results;
    }

    public static Pair<Ticket, TicketCustom> parameterToTicket(Map<String, String[]> parameter, Ticket t, TicketCustom tc) {
    	//ticket
        t.setId(Long.parseLong(parameter.get("id")[0]));
        t.setType(parameter.get("type")[0]);
        t.setTime(Long.parseLong(parameter.get("time")[0]));
        t.setChangetime(Long.parseLong(parameter.get("changetime")[0]));
        t.setComponent(parameter.get("component")[0]);
        t.setSeverity(parameter.get("severity")[0]);
        t.setOwner(parameter.get("owner")[0]);
        t.setReporter(parameter.get("reporter")[0]);
        t.setCc(parameter.get("cc")[0]);
        t.setVersion(parameter.get("version")[0]);
        t.setMilestone(parameter.get("milestone")[0]);
        if (parameter.containsKey("status")) {
            t.setStatus(parameter.get("status")[0]);
        }
        if (Status.find(t.getStatus()) == Status.CLOSED) {
            t.setResolution(parameter.get("resolution")[0]);
        } else {
            t.setResolution(null);
        }
        t.setSummary(parameter.get("summary")[0]);
        t.setDescription(parameter.get("description")[0]);
        t.setKeywords(parameter.get("keywords")[0]);

        //ticketCustom
        tc.setId(t.getId());
        for (TicketCustomFields tcf : TicketCustomFields.values()) {
            if (parameter.containsKey(tcf.toString())) {
                tc.setValue(tcf.toString(), parameter.get(tcf.toString())[0]);
            }
        }
        //checkbox用(2つ以上になるならメソッドを分ける
        if (parameter.containsKey("pre_release")){
        	if (parameter.get("pre_release")[0].equals("on")){
        		tc.setValue("pre_release", "1");
        	}else{
        		tc.setValue("pre_release", "0");
        	}
        	
        }

        return new ImmutablePair<Ticket, TicketCustom>(t, tc);
    }
}
