package iz.tracex.dto.search;

import iz.tracex.dto.trac.ini.FieldTranslater;
import iz.tracex.dto.trac.ini.Status;
import iz.tracex.dto.trac.ini.TicketFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class Criteria {
	public List<String> ticketIds = new ArrayList<String>();
    public StatusValue[] statusValues = new StatusValue[8];
    public List<String> milestones = new ArrayList<String>();
    public List<String> owners = new ArrayList<String>();
    public List<String> keywords = new ArrayList<String>();
    public List<String> components = new ArrayList<String>();
    public List<String> description = new ArrayList<String>();
    public List<String> summary = new ArrayList<String>();
    public List<String> productFiles = new ArrayList<String>();
    public List<String> idsFromElastic = new ArrayList<String>();
    
    public Map<SortValue,String> sortFields = new LinkedHashMap<>();
    public Map<String, String> displayFields = new HashMap<>();
    
    
    //customフィールドとインスタンス
    public CriteriaCustom custom = new CriteriaCustom();
    public DetailWhereCondition condition = new DetailWhereCondition();

    /**
     * デフォルト設定
     */
    public Criteria() {
        statusValues[0] = new StatusValue(Status.NEW, true);
        statusValues[1] = new StatusValue(Status.ASSIGNED, true);
        statusValues[2] = new StatusValue(Status.SUBMITTED, true);
        statusValues[3] = new StatusValue(Status.EVALUATING, false);
        statusValues[4] = new StatusValue(Status.EVALUATED, false);
        statusValues[5] = new StatusValue(Status.CLOSED, false);
        statusValues[6] = new StatusValue(Status.BACKED, true);
        statusValues[7] = new StatusValue(Status.SUSPENDED, false);
        displayFields.put(TicketFields.of("MILESTONE"), FieldTranslater.getJapaneseText(TicketFields.of("MILESTONE")));
        displayFields.put(TicketFields.of("SUMMARY"), FieldTranslater.getJapaneseText(TicketFields.of("SUMMARY")));
        displayFields.put(TicketFields.of("STATUS"), FieldTranslater.getJapaneseText(TicketFields.of("STATUS")));
        displayFields.put(TicketFields.of("OWNER"), FieldTranslater.getJapaneseText(TicketFields.of("OWNER")));
        displayFields.put(TicketFields.of("TYPE"), FieldTranslater.getJapaneseText(TicketFields.of("TYPE")));
        displayFields.put(TicketFields.of("SEVERITY"), FieldTranslater.getJapaneseText(TicketFields.of("SEVERITY")));
        condition.patricalMatchOrConcat.add("tag");
    }

    /**
     * チェックされたステータスを設定
     * @param statusStrings
     */
    public void setCheckedStatus(String[] statusStrings) {
        for (StatusValue statusValue : statusValues) {
            statusValue.checked = ArrayUtils.contains(statusStrings,
                    statusValue.status.toString());
        }
    }

    /**
     * チェックされたステータスを取得→検索で利用
     * @return
     */
    public Status[] getCheckedStatus() {
        Set<Status> result = new HashSet<>();
        for (StatusValue statusValue : statusValues) {
            if (statusValue.checked) {
                result.add(statusValue.status);
            }
        }
        return result.toArray(new Status[result.size()]);
    }

    /**
     * グルーピング対象カラムを返す(linkedhashmap)
     * @return
     */
    public String getGroupingField() {
    	for (SortValue sv : sortFields.keySet()){
    		return  sv.field;
    	}
    	return null;
    }
    
}
