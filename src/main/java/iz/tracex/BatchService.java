package iz.tracex;

import iz.tracex.base.elasticsearch.dao.TicketCustomDao;
import iz.tracex.base.elasticsearch.dto.TicketCustom;
import iz.tracex.dao.DocDao;
import iz.tracex.dao.TicketDao;
import iz.tracex.dto.trac.Doc;
import iz.tracex.dto.trac.Ticket;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

public class BatchService {
	private static final Logger	logger	= LoggerFactory.getLogger(BatchService.class);
	final Client	            client;
	
	BatchService(Client client) {
		this.client = client;
	}
	
	public void execute() throws JsonProcessingException, IOException {
		//全部IDがチケットIDになるように調整が必要
		deleteAll();
		insertTicket();
		insertDoc();
		delInsertTicketCustom();
	}
	
	private void deleteAll(){
		client.prepareDeleteByQuery("trac")
		.setQuery(QueryBuilders.matchAllQuery())
		.execute().actionGet();	
	}
	
	private void delInsertTicketCustom() {
		//10000件ずつ繰り返す
		List<TicketCustom> tcs = new TicketCustomDao().selectAll();
		
		int procCnt = 0;
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (TicketCustom t : tcs) {
			bulkRequest.add(client.prepareIndex("trac", "ticket_custom").setSource(new Gson().toJson(t)));
			procCnt ++;
			if (procCnt % 10000 == 0){
				BulkResponse res = bulkRequest.execute().actionGet();
				System.out.println(res);
				bulkRequest = client.prepareBulk();
			}
		}
		BulkResponse res = bulkRequest.execute().actionGet();
		System.out.println(res);
		
	}
	
	private void insertTicket() throws JsonProcessingException, IOException {
		List<Ticket> ticket = new TicketDao().selectAll();
		logger.debug("get ticket !");
				
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Ticket t : ticket) {
			String json = new Gson().toJson(t);
			
			ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(json);
	        ((ObjectNode)root).set("ticketId", root.get("id"));
	        ((ObjectNode)root).remove("id");
	        
			bulkRequest.add(client.prepareIndex("trac", "ticket").setSource(root.toString()));
			logger.debug(String.valueOf(t.getId()));
		}
		BulkResponse res = bulkRequest.execute().actionGet();
		
		System.out.println(res);
	}
	
	private void insertDoc() {
		logger.debug("geting  doc !");
		List<Doc> docs = new DocDao().selectAll();
		logger.debug("get doc !");
		
		BulkRequestBuilder delBulk = client.prepareBulk();
		for (Doc d : docs) {
			d.setId(d.getTicket_id());
			delBulk.add(client.prepareDelete("trac", "doc", String.valueOf(d.getTicket_id())));
			logger.debug(String.valueOf(d.getTicket_id()));
		}
		System.out.println(delBulk.execute().actionGet());
		
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		for (Doc d : docs) {
			bulkRequest.add(client.prepareIndex("trac", "doc").setSource(new Gson().toJson(d)));
			logger.debug(String.valueOf(d.getTicket_id()));
		}
		BulkResponse res = bulkRequest.execute().actionGet();
		
		System.out.println(res);
		
	}
	
}
