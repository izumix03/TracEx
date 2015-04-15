package iz.tracex.servlet.dispatcher;

import iz.tracex.front.ajax.AttachmentController;
import iz.tracex.front.ajax.ClientsController;
import iz.tracex.front.ajax.ComponentController;
import iz.tracex.front.ajax.ComponentsController;
import iz.tracex.front.ajax.ConfirmUpdated;
import iz.tracex.front.ajax.DetailDocController;
import iz.tracex.front.ajax.FunctionGroupController;
import iz.tracex.front.ajax.FunctionGroupsController;
import iz.tracex.front.ajax.LoginController;
import iz.tracex.front.ajax.MemberController;
import iz.tracex.front.ajax.MembersController;
import iz.tracex.front.ajax.MilestoneController;
import iz.tracex.front.ajax.MilestonesController;
import iz.tracex.front.ajax.SubsystemController;
import iz.tracex.front.ajax.SubsystemsController;
import iz.tracex.front.ajax.SvnDiffController;

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
public enum AjaxDispatcher {
	//UsrTblを返すと認証に入るよ
    MILESTONES(new MilestonesController()),
    MILESTONE(new MilestoneController()),
    MEMBERS(new MembersController()),
    MEMBER(new MemberController()),
    COMPONENTS(new ComponentsController()),
    COMPONENT(new ComponentController()),
    SUBSYSTEMS(new SubsystemsController()),
    SUBSYSTEM(new SubsystemController()),
    FUNCTION_GROUPS(new FunctionGroupsController()),
    FUNCTION_GROUP(new FunctionGroupController()),
    SVNDIFF(new SvnDiffController()),
    DETAILDOC(new DetailDocController()),
    LOGIN(new LoginController()),
    UPDATED(new ConfirmUpdated()),
    CLIENTS(new ClientsController()),
    ATTACHMENT(new AttachmentController());

    private static final Logger logger = LoggerFactory.getLogger(AjaxDispatcher.class);

    private final AjaxController<?> controller;

    private AjaxDispatcher(AjaxController<?> controller) {
        this.controller = controller;
    }

    public static AjaxDispatcher of(String name) {
        for (AjaxDispatcher e : AjaxDispatcher.values()) {
            if (StringUtils.equalsIgnoreCase(e.toString(), name)) {
                return e;
            }
        }
        return null;
    }

    public Object dispatch(HttpServletRequest request, String userId) {
        final StopWatch sw = new StopWatch();
        sw.start();
        try {
        	return controller.process(request.getParameterMap(), userId);
        } finally {
            logger.debug("Ajax process {} time = {}ms", toString(), sw.getTime());
        }
    }
}
