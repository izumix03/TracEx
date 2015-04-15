package iz.tracex.dto.trac;

import iz.tracex.base.TracExUtils;

/**
 *
 * @author izumi_j
 *
 */
public final class Doc{

    public String changetimeStr() {
        return changetime > 0 ? TracExUtils.unixTimeToDateTimeStr(changetime) : "----/--/--";
    }
    
    private long id;
	private long ticket_id;
//	private String doc_subsystem;
//	private String doc_function;
//	private String doc_dev;
//	private String doc_eva;
//	private String doc_trono;
	private String doc_devno;
//	private String doc_tester; 
//	private String doc_test_status;
	private String doc_problem;
	private String doc_solution;
	private String doc_tekiyou;
//	private String doc_kakunin;
//	private String doc_file_client_web;
//	private String doc_file_client_company;
//	private String doc_file_client_etc;
//	private String doc_file_client_tool;
//	private String doc_file_other;
	private String doc_type;
//	private String doc_ver;
	private String doc_fuguai_g;
	private String doc_kakunin_g;
	private String doc_siyou_g;
//	private String doc_customer;
//	private String doc_commit;
//	private String doc_route1;
//	private String doc_route2;
//	private String doc_route3;
//	private String doc_progress;
//	private String doc_bug_cause;
//	private String doc_bug_pattern;
//	private String doc_bug_report;
//	private String doc_file_scope;
//	private String doc_file_after;
//	private String doc_file_sort_type;
	private String doc_related_ticket;
//	private String doc_product_file_manual_flg;
//	private String doc_product_file_path;
	private String doc_exe_file_name;
//	private String doc_tekiyou_ichi;
	private String doc_tekiyou_comment;
	private String doc_tekiyou_junjo;
//	private String doc_bikou;
//	private String doc_sokudorekka_flg;
//	private String doc_kouzokueikyou_flg;
//	private String doc_setteihenko_flg;
//	private String doc_manualshusei_flg;
//	private String doc_lock_flg;
	private String doc_shanai_flg;
//	private String doc_tekiyou_junjo_multi_flg;
//	private String doc_tekiyou_junjo_1x;
//	private String doc_tekiyou_junjo_2x;
//	private String doc_tekiyou_junjo_31;
//	private String doc_tekiyou_junjo_32;
//	private String doc_tekiyou_junjo_trunk;
//	private String doc_problem_multi_flg;
//	private String doc_problem_1x;
//	private String doc_problem_2x;
//	private String doc_problem_31;
//	private String doc_problem_32;
//	private String doc_solution_1x;
//	private String doc_solution_2x;
//	private String doc_solution_31;
//	private String doc_solution_32;
//	private String doc_tekiyou_junjo_33;
//	private String doc_problem_33;
//	private String doc_solution_33;
//	private String doc_exe_file_name_multi_flg;
//	private String doc_exe_file_name_1x;
//	private String doc_exe_file_name_2x;
//	private String doc_exe_file_name_31;
//	private String doc_exe_file_name_32;
//	private String doc_exe_file_name_33;
	private long changetime;
//	private String doc_tekiyou_junjo_34;
//	private String doc_problem_34;
//	private String doc_solution_34;
//	private String doc_exe_file_name_34;
//	private String doc_problem_kaiji;
//	private String doc_solution_kaiji;
//	private String doc_tekiyou_junjo_35;
//	private String doc_problem_35;
//	private String doc_solution_35;
//	private String doc_exe_file_name_35;
//	private String doc_tekiyou_junjo_36;
//	private String doc_problem_36;
//	private String doc_solution_36;
//	private String doc_exe_file_name_36;
    private static final String[] fuguaiList = {
    	"<a-01>(画面)画面の異常終了・フリーズ","<a-02>(画面)画面表示されない","<a-03>(画面)画面の表示内容が不備","<a-04>(画面)画面の動作が不備","<a-05>(画面)ファイルI/O","<a-06>(画面)データ更新","<b-01>(バッチ処理)バッチ処理の動作が不備","<b-02>(バッチ処理)バッチ処理が異常終了","<b-03>(バッチ処理)バッチ処理のステータスが不備","<b-04>(バッチ処理)バッチ処理が設定どおりに動作しない","<b-05>(バッチ処理)ファイルI/O","<b-06>(バッチ処理)データ更新","<c-01>(帳票)帳票が出力されない","<c-02>(帳票)帳票の表示内容不備","<d-01>セキュリティ","<d-02>ワークフロー","<d-03>自動仕訳・GL連携","<d-04>他社製品への対応","<e-01>外部連携-ACAI","<e-02>外部連携-ACAP","<e-03>外部連携-APIJOBKICER対応等","<f-01>パフォーマンス","<z-99>その他"
    };
    private static final String[] kakuninList={
    	"<a-01>設定・運用の見直しを要する","<a-02>既存データの修正を要する","<c-01>共通変更により、他プロダクトのバージョンアップを要する"
    };
    private static final String[] siyouList={
    	"<a-01>画面で表示するレコード(伝票や取引先など)の読込条件や表示条件の変更","<a-02>画面でのデータ操作（登録・更新・削除）内容の変更","<a-03>画面の動作の変更","<b-01>表示項目の新規追加または削除","<b-02>画面の表記や文章の変更","<c-01>入力項目の新規追加または削除","<c-02>項目の入力チェック(数値チェック、NULLチェック、入力必須など)の変更","<c-03>コンボボックスの内容や文章の変更","<c-04>処理対象の変更（バッチ・画面）","<d-01>既存項目の配置またはタブオーダーの変更","<e-01>数値や時間数などの計算ロジックや端数処理の変更","<f-01>エラーメッセージの文章表現の変更","<f-02>エラー判定の条件・内容の変更","<g-01>画面項目(表記/文章/コンボボックスの内容など)のKubunまたはDict化による表記の変更","<h-01>Version情報やCOMPANYのロゴ、背景色など操作に関係ない画面デザインの変更","<i-01>帳票・CSV/EXCEL出力のレイアウト(罫線等も含む)・出力項目の変更","<i-02>帳票・CSV/EXCEL出力の出力条件の変更","<j-01>セキュリティ関連","<k-01>リニューアル","<k-02>廃止","<z-99>その他"
    };
    private static final String[] bikouList={
    	"修正による速度劣化の有無", "後続処理への影響の有無", "設定変更・画面レイアウト変更", "マニュアル修正"
    };
    private static final String[] docTextList={
    	"---","要修正","修正済","OK"
    };
    
    public String[] getDocTextList(){
    	return docTextList;
    }
        
    public boolean isCheckedFuguai(String val){
    	if (isNullOrEmpty(doc_fuguai_g)){
    		return false;
    	}
    	return (doc_fuguai_g.indexOf(val) != -1);
    }
    public boolean isCheckedKakunin(String val){
    	if (isNullOrEmpty(doc_kakunin_g)){
    		return false;
    	}
    	return doc_kakunin_g.indexOf(val) != -1;
    }
    public boolean isCheckedSiyou(String val){
    	if (isNullOrEmpty(doc_siyou_g)){
    		return false;
    	}
    	return doc_siyou_g.indexOf(val) != -1;
    }
    
    public String[] getSiyouList(){
    	return siyouList;
    }
    
    public String[] getKakuninList(){
    	return kakuninList;
    }
    public String[] getFuguaiList(){
    	return fuguaiList;
    }
    
    public String[] getBikouList(){
    	return bikouList;
    }

    public long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(long ticket_id) {
        this.ticket_id = ticket_id;
    }


	public String getDoc_devno() {
		return doc_devno;
	}

	public void setDoc_devno(String doc_devno) {
		this.doc_devno = doc_devno;
	}


	public String getDoc_problem() {
		return doc_problem;
	}

	public void setDoc_problem(String doc_problem) {
		this.doc_problem = doc_problem;
	}

	public String getDoc_solution() {
		return doc_solution;
	}

	public void setDoc_solution(String doc_solution) {
		this.doc_solution = doc_solution;
	}

	public String getDoc_tekiyou() {
		return doc_tekiyou;
	}

	public void setDoc_tekiyou(String doc_tekiyou) {
		this.doc_tekiyou = doc_tekiyou;
	}

	public String getDoc_type() {
		return doc_type;
	}

	public void setDoc_type(String doc_type) {
		this.doc_type = doc_type;
	}


	public String getDoc_fuguai_g() {
		return doc_fuguai_g;
	}

	public void setDoc_fuguai_g(String doc_fuguai_g) {
		this.doc_fuguai_g = doc_fuguai_g;
	}

	public String getDoc_kakunin_g() {
		return doc_kakunin_g;
	}

	public void setDoc_kakunin_g(String doc_kakunin_g) {
		this.doc_kakunin_g = doc_kakunin_g;
	}

	public String getDoc_siyou_g() {
		return doc_siyou_g;
	}

	public void setDoc_siyou_g(String doc_siyou_g) {
		this.doc_siyou_g = doc_siyou_g;
	}


	public String getDoc_related_ticket() {
		return doc_related_ticket;
	}

	public void setDoc_related_ticket(String doc_related_ticket) {
		this.doc_related_ticket = doc_related_ticket;
	}


	public String getDoc_exe_file_name() {
		return doc_exe_file_name;
	}

	public void setDoc_exe_file_name(String doc_exe_file_name) {
		this.doc_exe_file_name = doc_exe_file_name;
	}

	public String getDoc_tekiyou_comment() {
		return doc_tekiyou_comment;
	}

	public void setDoc_tekiyou_comment(String doc_tekiyou_comment) {
		this.doc_tekiyou_comment = doc_tekiyou_comment;
	}

	public String getDoc_tekiyou_junjo() {
		return doc_tekiyou_junjo;
	}

	public void setDoc_tekiyou_junjo(String doc_tekiyou_junjo) {
		this.doc_tekiyou_junjo = doc_tekiyou_junjo;
	}

	public String getDoc_shanai_flg() {
		return doc_shanai_flg;
	}

	public void setDoc_shanai_flg(String doc_shanai_flg) {
		this.doc_shanai_flg = doc_shanai_flg;
	}

	public long getChangetime() {
		return changetime;
	}

	public void setChangetime(long changetime) {
		this.changetime = changetime;
	}

	private boolean isNullOrEmpty(String str){
		return (str == null || str.length() == 0);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
