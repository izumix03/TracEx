package iz.tracex;

import iz.tracex.base.elasticsearch.ClientManager;
import iz.tracex.base.elasticsearch.dao.TicketCustomDao;
import iz.tracex.base.elasticsearch.dto.TicketCustom;
import iz.tracex.dao.TicketDao;
import iz.tracex.dto.search.CriteriaCustom;
import iz.tracex.dto.trac.Ticket;
import iz.tracex.dto.trac.ini.DisplayFields;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

/**
 * 
 * @author izumi_j
 * 
 */
public class WorkTest {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkTest.class);

	@Test
	public void deleteAll(){
		ClientManager.getClient().prepareDeleteByQuery("trac")
		.setQuery(QueryBuilders.matchAllQuery())
		.execute().actionGet();
		
	}
	
	
	@Test
	public void test2(){
		Class<?> clazz = CriteriaCustom.class;
        for (Field f: clazz.getDeclaredFields()){
        	System.out.println(f.getName());
        	if (f.getType().isArray()){
        		System.out.println(Array.getLength(f.getType()));
        	}else{
        		System.out.println(f.getType());
        	}
        }		
	}
	
	@Test
	public void jsonTest() throws JsonProcessingException, IOException{
		Ticket ticket = new TicketDao().selectBy(1);
		String json = new Gson().toJson(ticket);
		
		ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        ((ObjectNode)root).set("ticketId", root.get("id"));
        ((ObjectNode)root).remove("id");
        
		System.out.println(root.toString());
	}
	
	@Test
	public void test() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		
		Class<?> clazz = DisplayFields.class;
		Class<?> sClazz = clazz.getSuperclass();
		
		Field[] ticketFields = sClazz.getDeclaredFields();
		for (Field f : ticketFields){
			sb.append(f.getName());
			sb.append(", ");
		}
		
		Field[] ticketCustomFields = clazz.getDeclaredFields();
		for (int i = 0; i < ticketCustomFields.length ;i++){
			sb.append(" (select value from ticket_custom where ticket = id and name='");
			sb.append(ticketCustomFields[i].getName());
			if (i != ticketCustomFields.length-1){
				sb.append("'), ");
			}else{
				sb.append("') ");
			}
		}
				
		sb.append(" FROM TICKET ");
		System.out.println(sb.toString());
	}
	
	@Test
	public void test3() throws AddressException, MessagingException, IOException{
		TicketCustomDao dao = new TicketCustomDao();
		int maxCnt = dao.getCount(); 
		int procCnt = 0;
		
		while (procCnt < maxCnt){
			List<TicketCustom> tcs = dao.selectLimit(procCnt, 50000);
			
			procCnt += tcs.size();
		}		
	}

	
}
