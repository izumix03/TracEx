package iz.tracex.dto.search;

import java.util.ArrayList;
import java.util.List;

/**
 * カラムごとの検索条件指定方法
 * @author izumikawa_t
 */
public class DetailWhereCondition {
	public List<String> patricalMatchOrConcat = new ArrayList<String>();
	public List<String> patricalMatchAndConcat = new ArrayList<String>();
	
	//何もなければIN
	
}
