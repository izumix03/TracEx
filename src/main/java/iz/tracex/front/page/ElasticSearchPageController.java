package iz.tracex.front.page;

import iz.tracex.front.page.helper.ElasticSearchController;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

public class ElasticSearchPageController implements PageController {
	private final ElasticSearchController	elastic	= new ElasticSearchController();
	
	@Override
	public PageResult process(Map<String, String[]> parameter, String userId) {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		if (parameter.containsKey("word")) {
			result.put("word", parameter.get("word"));
			
			// Elastic Serach
			final StopWatch sw = new StopWatch();
			List<String> ticketIds = elastic.getTicketsIncludingText(parameter.get("word")[0]);
			result.put("idsFromElastic", ticketIds.size() != 0? (String[])ticketIds.toArray(new String[ticketIds.size()]):new String[0] );
			result.put("do-search",new String[0]);
		}
				
		return new ListController().process(result, userId);
	}
}
