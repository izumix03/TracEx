package iz.tracex;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;
import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.translate.User;
import iz.tracex.servlet.JettyHandler;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author izumi_j
 *
 */
public final class TracExService implements Daemon {
    private static final Logger logger = LoggerFactory.getLogger(TracExService.class);

    private static final TracExService INSTANCE = new TracExService();

    private final boolean launchBrowser;
    private Server jetty = null;
    
    private TracExService() {
        launchBrowser = false;
    }

    public TracExService(boolean launchBrowser) {
        this.launchBrowser = launchBrowser;
    }

    /**
     * For Windows.
     *
     * @param args
     * @throws Exception
     */
    public static void startService(String[] args) throws Exception {
        INSTANCE.start();
    }

    /**
     * For Windows.
     *
     * @param args
     * @throws Exception
     */
    public static void stopService(String[] args) throws Exception {
        INSTANCE.stop();
    }

    /**
     * For Linux.
     */
    @Override
    public void init(DaemonContext context) throws Exception {
        // TODO

    }

    /**
     * For Linux.
     */
    @Override
    public void destroy() {
        // TODO
    }

    @Override
    public void start() throws Exception {
        logger.info("Start service.");

        validateUser();

        int port = TracExProperties.getInt(Name.PORT);
        logger.info("Port number = {}", port);

        jetty = new Server(port);
        
        // Specify the Session ID Manager
        HashSessionIdManager idManager = new HashSessionIdManager();
        jetty.setSessionIdManager(idManager);
        
        // Sessoins are bount to a context
        ContextHandler context = new ContextHandler("/");
        jetty.setHandler(context);
        
        // Create the SessionHandler(wrapper) to handle the sessions
        HashSessionManager manager = new HashSessionManager();
        SessionHandler sessons = new SessionHandler(manager);
        context.setHandler(sessons);
        
        // Put dump inside of SessionHandler 
        sessons.setHandler(new JettyHandler());
        
        try {
            jetty.start();

            if (launchBrowser) {
                launchBrowser(port);
            }

            jetty.join();

        } catch (Exception e) {
            logger.error("Error occurred!", e);
            throw e;
        }
    }

    @Override
    public void stop() throws Exception {
        jetty.stop();

        logger.info("Stop service.");
    }

    private void validateUser() {
        final String userId = TracExProperties.getString(Name.USER);
        if (StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException("[user] is required in TracEx.properties!");
        }
        final UserDao userDao = new UserDao();
        final User user = userDao.selectBy(userId);
        if (user == null) {
            throw new IllegalArgumentException(userId + " is invalid user!");
        }
    }

    private void launchBrowser(int port) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URI.create(StringUtils.join("http://localhost:", String.valueOf(port), "/index")));
    }
}
