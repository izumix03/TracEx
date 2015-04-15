package iz.tracex.dto.trac.ini;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FieldTranslater {
	//setter(japanese name)配列は例外メンバ
  	private static final Map<String, String> FIELD_TRANSLATE_MAP;
  	static{
  		Map<String, String> map = new HashMap<String, String>();
  		map.put("id", "チケット");
  		map.put("type", "分類");
  		map.put("time", "登録日時");
  		map.put("changetime", "最終変更日時");
  		map.put("component", "コンポーネント");
  		map.put("severity", "重要度");
  		map.put("priority", "緊急度");
  		map.put("owner", "開発者");
  		map.put("reporter", "報告者");
  		map.put("cc", "関係者");
  		map.put("version", "バージョン");
  		map.put("milestone", "マイルストーン");
  		map.put("status", "ステータス");
  		map.put("resolution", "解決方法");
  		map.put("summary", "概要");
  		map.put("description", "説明");
  		map.put("keywords", "キーワード");
		map.put("evaluator", "評価者");
		map.put("occurrence_reason", "発生区分");
		map.put("client", "顧客名");
		map.put("pre_released", "暫定リリース");
		map.put("at_suport_trouble_no", "＠SUPPORTトラブル対応依頼番号");
		map.put("due_assign", "開始予定日");
		map.put("due_close", "終了予定日");
		map.put("developper_note", "開発者ノート");
		map.put("zoo_huguai_no", "ZOO不具合番号");
		map.put("function_group", "機能分類");
		map.put("apply_div", "申請区分");
		map.put("doc_tester_memo", "QA評価メモ");
		map.put("doctext_check", "doc状況");
		map.put("doc_check_memo", "doc指摘事項");
		map.put("tag", "タグ");
		map.put("subsystem", "サブシステム");
  		FIELD_TRANSLATE_MAP = Collections.unmodifiableMap(map);
  	}
  	
  	//getter(japanese name)
  	public static String getJapaneseText(String fieldName){
  		if (FIELD_TRANSLATE_MAP.containsKey(fieldName)){
  			return FIELD_TRANSLATE_MAP.get(fieldName);
  		}else{
  			return fieldName;
  		}
  	}
  	
}
