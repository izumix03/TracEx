package iz.tracex;

import iz.tracex.base.elasticsearch.ClientManager;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Elastic Searchへデータを投入するバッチ(Main)
 * @author works780
 *
 */
public final class BatchMain {
	private static final Logger logger = LoggerFactory.getLogger(BatchMain.class);
	
	private BatchMain(){
	}
	
	public static void main(String[] args){
		final StopWatch sw = new StopWatch();
		sw.start();
		try{			
			new BatchService(ClientManager.getClient()).execute();
		}catch(Throwable e){
			logger.warn("Failed to collect!", e);
		}finally{
			sw.stop();
			logger.info("Batch finished. time = {}s", Math.floor(sw.getTime() / 1000));
		}
	}
	
}
