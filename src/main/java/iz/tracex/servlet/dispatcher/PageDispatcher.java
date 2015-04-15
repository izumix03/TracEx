package iz.tracex.servlet.dispatcher;

import iz.tracex.front.page.DetailController;
import iz.tracex.front.page.ElasticSearchPageController;
import iz.tracex.front.page.IndexController;
import iz.tracex.front.page.ListController;
import iz.tracex.front.page.SvnDiffController;
import iz.tracex.front.page.UserController;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public enum PageDispatcher {
	INDEX(new IndexController()), LIST(new ListController()), DETAIL(new DetailController()), SVN_DIFF(new SvnDiffController()), USER(
	        new UserController()), ELASTICSEARCH(new ElasticSearchPageController());
	
	private static final Logger	 logger	= LoggerFactory.getLogger(PageDispatcher.class);
	
	private final PageController	controller;
	
	private PageDispatcher(PageController controller) {
		this.controller = controller;
	}
	
	public static PageDispatcher of(String name) {
		for (PageDispatcher e : PageDispatcher.values()) {
			if (StringUtils.equalsIgnoreCase(e.toString(), name)) {
				return e;
			}
		}
		return null;
	}
	
	public String identifier() {
		//Elastic searchのリダイレクト用
		return StringUtils.lowerCase(name()).equals("elasticsearch") ? 
				StringUtils.lowerCase(PageDispatcher.LIST.name()) : StringUtils.lowerCase(name());
	}
	
	public PageResult dispatch(HttpServletRequest request, String userId) {
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			return controller.process(request.getParameterMap(), userId);
		} finally {
			logger.debug("Page process {} time = {}ms", toString(), sw.getTime());
		}
	}
}
