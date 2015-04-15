package iz.tracex.dto.trac.ini;

import iz.tracex.dto.trac.Ticket;

/**
 * 一覧表示項目_全部Stringでいいやもう
 * @author izumikawa_t
 *
 */
public class DisplayFields extends Ticket{
	private String evaluator;
	private String occurrence_reason;
	private String client;
	private String pre_released;
	private String at_suport_trouble_no;
	private String due_assign;
	private String due_close;
	private String developper_note;
	private String zoo_huguai_no;
	private String subsystem;
	private String function_group;
	private String apply_div;
	private String doc_tester_memo;
	private String doctext_check;
	private String doc_check_memo;
	private String tag;
	
	//getter and setter
	public String getEvaluator() {
		return evaluator;
	}
	public void setEvaluator(String evaluator) {
		this.evaluator = evaluator;
	}
	public String getOccurrence_reason() {
		return occurrence_reason;
	}
	public void setOccurrence_reason(String occurrence_reason) {
		this.occurrence_reason = occurrence_reason;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getPre_released() {
		return pre_released;
	}
	public void setPre_released(String pre_released) {
		this.pre_released = pre_released;
	}
	public String getAt_suport_trouble_no() {
		return at_suport_trouble_no;
	}
	public void setAt_suport_trouble_no(String at_suport_trouble_no) {
		this.at_suport_trouble_no = at_suport_trouble_no;
	}
	public String getDue_assign() {
		return due_assign;
	}
	public void setDue_assign(String due_assign) {
		this.due_assign = due_assign;
	}
	public String getDue_close() {
		return due_close;
	}
	public void setDue_close(String due_close) {
		this.due_close = due_close;
	}
	public String getDevelopper_note() {
		return developper_note;
	}
	public void setDevelopper_note(String developper_note) {
		this.developper_note = developper_note;
	}
	public String getZoo_huguai_no() {
		return zoo_huguai_no;
	}
	public void setZoo_huguai_no(String zoo_huguai_no) {
		this.zoo_huguai_no = zoo_huguai_no;
	}
	public String getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}
	public String getFunction_group() {
		return function_group;
	}
	public void setFunction_group(String function_group) {
		this.function_group = function_group;
	}
	public String getApply_div() {
		return apply_div;
	}
	public void setApply_div(String apply_div) {
		this.apply_div = apply_div;
	}
	public String getDoc_tester_memo() {
		return doc_tester_memo;
	}
	public void setDoc_tester_memo(String doc_tester_memo) {
		this.doc_tester_memo = doc_tester_memo;
	}
	public String getDoctext_check() {
		return doctext_check;
	}
	public void setDoctext_check(String doctext_check) {
		this.doctext_check = doctext_check;
	}
	public String getDoc_check_memo() {
		return doc_check_memo;
	}
	public void setDoc_check_memo(String doc_check_memo) {
		this.doc_check_memo = doc_check_memo;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
		
}
