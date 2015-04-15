package iz.tracex.front.ajax;

import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.translate.User;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class MemberController implements AjaxController<User> {
    private final UserDao dao = new UserDao();

    @Override
    public User process(Map<String, String[]> parameter, String userId) {
        final String name = parameter.containsKey("value") ? parameter.get("value")[0] : null;
        return dao.selectBy(name);
    }

}
