package iz.tracex.base.elasticsearch;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author izumikawa_t
 * 
 */
public final class ClientManager {
	private static final Logger logger = LoggerFactory.getLogger(ClientManager.class);
	
	private static final Settings	settings;
	static {
		settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
	}
	
	private static Client client;
	static {
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(TracExProperties.getString(Name.ELASTICIP), Integer
		        .parseInt(TracExProperties.getString(Name.ELASTICPORT))));
	}
	
	/**
	 * @return client
	 */
	public static Client getClient() {
		if (client == null) {
			try {
				client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(TracExProperties.getString(Name.ELASTICIP),
				        Integer.parseInt(TracExProperties.getString(Name.ELASTICPORT))));
				logger.info("oh my god!!");
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return client;
	}
}
