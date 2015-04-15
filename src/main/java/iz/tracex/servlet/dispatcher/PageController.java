package iz.tracex.servlet.dispatcher;

import java.util.Map;

/**
 *  Be thread safe!
 *
 * @author izumi_j
 *
 */
public interface PageController {

    PageResult process(Map<String, String[]> parameter, String userId);
}
