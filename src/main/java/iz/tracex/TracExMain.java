package iz.tracex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class TracExMain {
    private static final Logger logger = LoggerFactory.getLogger(TracExMain.class);

    /**
     * @param args
     */
    public static void main(String args[]) {
        logger.info("Start!");

        try {
            new TracExService(true).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.info("End!");
    }

}
