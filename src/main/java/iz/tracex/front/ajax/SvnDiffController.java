package iz.tracex.front.ajax;

import iz.tracex.dto.trac.translate.SvnDiff;
import iz.tracex.front.page.helper.SvnDiffMaker;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.wc.SVNRevision;

public final class SvnDiffController implements AjaxController<List<SvnDiff>> {
	private final SvnDiffMaker dm = new SvnDiffMaker();
		
	@Override
	public List<SvnDiff> process(Map<String, String[]> parameter, String userId) {
		final long rev= parameter.containsKey("rev") ? Long.parseLong(parameter.get("rev")[0]) : 0;
		final String fromPath = parameter.containsKey("fromPath") ? parameter.get("fromPath")[0] :"";
		final String toPath = parameter.containsKey("toPath") ? parameter.get("toPath")[0]: "";
		
		return dm.getDiff(SVNRevision.create(rev - 1), SVNRevision.create(rev), fromPath, toPath);
	}
	
}
