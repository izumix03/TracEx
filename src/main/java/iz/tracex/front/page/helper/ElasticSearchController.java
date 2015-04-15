package iz.tracex.front.page.helper;

import iz.tracex.base.elasticsearch.ClientManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * 検索ワードを受け取ってTicketIdのリストを返す
 * @author izumikawa_t
 * 
 */
public class ElasticSearchController {
	
	public List<String> getTicketsIncludingText(String text) {
		//setTypes("ticket").
		SearchResponse res = ClientManager.getClient().prepareSearch("trac").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.multiMatchQuery(text, "_all")).setSize(500).setExplain(false).execute().actionGet();
		SearchHits hits = res.getHits();
		
		if (hits.getTotalHits()==0){
			return Collections.emptyList();
		}
		return addTicketIds(hits);
	}
	
	private List<String>  addTicketIds(SearchHits hits){
		List<String> tikectIdList = new ArrayList<String>();
		for (SearchHit h: hits){
			Object ticketId = h.getSource().get("ticketId");
			if (ticketId != null && !tikectIdList.contains(String.valueOf(ticketId))){
				tikectIdList.add(String.valueOf(ticketId));
			}
		}
		return tikectIdList;
	}
	
}
