package iz.tracex.front.page;

import iz.tracex.dao.DisplayFieldsDao;
import iz.tracex.dto.search.Criteria;
import iz.tracex.dto.search.CriteriaCustom;
import iz.tracex.dto.search.SortValue;
import iz.tracex.dto.trac.ini.DisplayFields;
import iz.tracex.dto.trac.ini.FieldTranslater;
import iz.tracex.dto.trac.ini.TicketFields;
import iz.tracex.front.page.helper.DefValueCollector;
import iz.tracex.front.page.helper.TicketConverter;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 検索画面表示
 * @author izumi_j
 */
public class ListController implements PageController {
    private final DisplayFieldsDao displaytDao = new DisplayFieldsDao();
    
    @Override
    public PageResult process(Map<String, String[]> parameter, String userId) {
        final Map<String, Object> result = new HashMap<>();

        // 定義値など
        result.putAll(DefValueCollector.collect());
        result.put("ticketFields", TicketFields.FIELDS_MAP);
        
        // 検索条件
        Criteria c = createCriteria(parameter);
        // TODO:画面から渡すようにする
        if (parameter.containsKey("word")){
        	result.put("word", parameter.get("word")[0]);
        	 c.displayFields.put(TicketFields.of("MILESTONE"), FieldTranslater.getJapaneseText(TicketFields.of("MILESTONE")));
        	 c.displayFields.put(TicketFields.of("SUMMARY"), FieldTranslater.getJapaneseText(TicketFields.of("SUMMARY")));
        	 c.displayFields.put(TicketFields.of("STATUS"), FieldTranslater.getJapaneseText(TicketFields.of("STATUS")));
        	 c.displayFields.put(TicketFields.of("OWNER"), FieldTranslater.getJapaneseText(TicketFields.of("OWNER")));
        	 c.displayFields.put(TicketFields.of("TYPE"), FieldTranslater.getJapaneseText(TicketFields.of("TYPE")));
        	 c.displayFields.put(TicketFields.of("SEVERITY"), FieldTranslater.getJapaneseText(TicketFields.of("SEVERITY")));
        }
        result.put("criteria", c);
        
        if (parameter.containsKey("do-search")) {
        	// チケット一覧取得_通常検索(or ElasticSearch)
            final List<DisplayFields> items = displaytDao.selectBy(c);
            result.put("groupBy", parameter.containsKey("groupBy"));
            result.put("results", TicketConverter.ticketsToMaps(items, parameter.containsKey("groupBy") ? c.getGroupingField(): null));
            result.put("total", items.size());
        } else {
        	//検索なし
            result.put("results", Collections.emptyList());
            result.put("total", 0);
        }
        
        // タイトル
        if (getFirstString(c.milestones) != null){
        	result.put("title", String.format("【%s】カスタムクエリ", getFirstString(c.milestones)));
        }else{
        	result.put("title", "カスタムクエリ");
        }
        
        return PageResult.create(result);
    }
    
    /**
     * リストの値がある最初を返す
     */
    private String getFirstString(List<String> args){
    	for (String s: args){
    		if (s != null && !s.isEmpty() && !s.equals("")){
    			return s;
    		}
    	}
		return null;
    }
    
    /**
     * 検索条件作成
     * @param parameter
     * @return
     */
    private Criteria createCriteria(Map<String, String[]> parameter) {
        final Criteria c = new Criteria();
        if (!parameter.containsKey("do-search")) {
            return c;
        }

        c.setCheckedStatus(new String[0]);
        if (parameter.containsKey("status")) {
            c.setCheckedStatus(parameter.get("status"));
        }
        if (parameter.containsKey("milestone")) {
            c.milestones = Arrays.asList(parameter.get("milestone"));
        }
        if (parameter.containsKey("owner")) {
            c.owners = Arrays.asList(parameter.get("owner"));
        }
        if (parameter.containsKey("component")) {
            c.components = Arrays.asList(parameter.get("component"));
        }
        if (parameter.containsKey("keywords")) {
            c.keywords = Arrays.asList(parameter.get("keywords"));
        }
        if (parameter.containsKey("components")) {
            c.components = Arrays.asList(parameter.get("components"));
        }
        if (parameter.containsKey("summary")) {
            c.summary = Arrays.asList(parameter.get("summary"));
        }
        if (parameter.containsKey("description")) {
            c.description = Arrays.asList(parameter.get("description"));
        }
        if (parameter.containsKey("ticketId")){
        	c.ticketIds = Arrays.asList(parameter.get("ticketId"));
        }
        if (parameter.containsKey("product-files")){
        	c.productFiles = Arrays.asList(parameter.get("product-files"));
        }
        if (parameter.containsKey("idsFromElastic")){
        	c.idsFromElastic = Arrays.asList(parameter.get("idsFromElastic"));
        }

        Class<?> clazz = CriteriaCustom.class;
        for (java.lang.reflect.Field f: clazz.getDeclaredFields()){
        	String k = f.getName();
        	if (parameter.containsKey(k)){
        		try{
        			f.setAccessible(true);
        			if (f.getType().isArray()){
        				f.set(c.custom, Arrays.copyOf(parameter.get(k), parameter.get(k).length));        				
        			}else{
        				try{
        					f.set(c.custom, parameter.get(k)[0]);
        				}catch (Exception p){
        					f.set(c.custom, Arrays.asList(parameter.get(k)));
        				}
        			}
        		}catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
        	}
        }
        

        if (parameter.containsKey("sort-field")) {
            final String[] fields = parameter.get("sort-field");
            final String[] methods = parameter.get("sort-method");
            for (int i = 0; i < fields.length; i++) {
                final String f = TicketFields.of(fields[i]);
                if (f != null) {
                    c.sortFields.put(new SortValue(f, StringUtils.equalsIgnoreCase(methods[i], "desc")), FieldTranslater.getJapaneseText(f));
                }
            }
        }

        c.displayFields.clear();
        if (parameter.containsKey("display-field")) {
        	for (String p: parameter.get("display-field")){
        		c.displayFields.put(p, FieldTranslater.getJapaneseText(p));
        	}
        }

        return c;
    }

}
