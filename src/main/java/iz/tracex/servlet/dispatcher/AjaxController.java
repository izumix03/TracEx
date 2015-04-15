package iz.tracex.servlet.dispatcher;

import java.util.Map;

/**
 * Be thread safe!
 *
 * @author izumi_j
 *
 */
public interface AjaxController<T> {

    T process(Map<String, String[]> parameter, String userId);

}
