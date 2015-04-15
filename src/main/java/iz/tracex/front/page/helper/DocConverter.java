package iz.tracex.front.page.helper;

import iz.tracex.dto.trac.Doc;
import iz.tracex.dto.trac.translate.TicketCustom;

import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author izumikawa_t
 *
 */
public final class DocConverter {
    private DocConverter() {
    }
    public static final String[] ticketCustomFieldsName = {"doctext_check", "doc_check_memo", "doc_tester_memo"};

    public static Pair<Doc, TicketCustom> parameterToDoc(Map<String, String[]> parameter, Doc d, TicketCustom tc) {
        d.setTicket_id(Long.parseLong(parameter.get("id")[0]));
        if (parameter.containsKey("doc_shanai_flg")){
        	if (parameter.get("doc_shanai_flg")[0].equals("on")){
        		d.setDoc_shanai_flg("1");
        	}else{
        		d.setDoc_shanai_flg("");
        	}
        }else{
        	d.setDoc_shanai_flg("0");
        }
        
        d.setDoc_devno(parameter.get("doc_devno")[0]);
        d.setDoc_related_ticket(parameter.get("doc_related_ticket")[0]);
        d.setChangetime(Long.parseLong(parameter.get("changetime")[0]));
        
        d.setDoc_type(parameter.get("doc_type")[0]);
        d.setDoc_problem(parameter.get("doc_problem")[0]);
        d.setDoc_solution(parameter.get("doc_solution")[0]);
        d.setDoc_exe_file_name(parameter.get("doc_exe_file_name")[0]);
        d.setDoc_tekiyou_comment(parameter.get("doc_tekiyou_comment")[0]);
        d.setDoc_tekiyou_junjo(parameter.get("doc_tekiyou_junjo")[0]);
        d.setDoc_tekiyou(parameter.get("doc_tekiyou")[0]);
        
        String[] docFuguaiG = parameter.get("doc_fuguai_g");
        String[] docKakuninG = parameter.get("doc_kakunin_g");
        String[] docSiyouG = parameter.get("doc_siyou_g");
        if (docFuguaiG != null){
        	d.setDoc_fuguai_g(getCheckedStringCommaDelim(docFuguaiG));
        }
        if (docKakuninG != null){
        	d.setDoc_kakunin_g(getCheckedStringCommaDelim(docKakuninG));
        }
        if (docSiyouG != null){
        	d.setDoc_siyou_g(getCheckedStringCommaDelim(docSiyouG));
        }
        
        //ticket_custom
        tc.setId(d.getTicket_id());
        for (String name : ticketCustomFieldsName) {
            if (parameter.containsKey(name)) {
                tc.setValue(name, parameter.get(name)[0]);
            }
        }

        return new ImmutablePair<Doc, TicketCustom>(d, tc);
    }
    
    private static String getCheckedStringCommaDelim(String[] param){
    	StringBuilder sb = new StringBuilder();
    	for (int i=0;i < param.length;i++){
        	if(i==0){
        		sb.append(param[i]);
        	}else{
        		sb.append(",");
        		sb.append(param[i]);
        	}
        }
    	return sb.toString();
    }
}
