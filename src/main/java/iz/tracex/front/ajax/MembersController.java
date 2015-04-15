package iz.tracex.front.ajax;

import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.translate.User;
import iz.tracex.servlet.dispatcher.AjaxController;

import java.util.List;
import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class MembersController implements AjaxController<List<User>> {
    private final UserDao dao = new UserDao();

    @Override
    public List<User> process(Map<String, String[]> parameter, String userId) {
        final String token = parameter.containsKey("query") ? parameter.get("query")[0] : null;
        return dao.selectMatchesBy(token);
    }

}
