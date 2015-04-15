package iz.tracex.front.page;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;
import iz.tracex.dao.UsrTblDao;
import iz.tracex.dto.trac.UsrTbl;
import iz.tracex.servlet.dispatcher.PageController;
import iz.tracex.servlet.dispatcher.PageResult;

import java.util.HashMap;
import java.util.Map;

/**
 * トップページ表示及びログイン処理
 * @author izumi_j
 */
public class IndexController implements PageController {
	private final UsrTblDao dao = new UsrTblDao();

    @Override
    public PageResult process(Map<String, String[]> parameter, String userId) {
    	
    	final String user_id = parameter.containsKey("user_id") ? parameter.get("user_id")[0] : null;
    	final String password = parameter.containsKey("password")? parameter.get("password")[0] : null;
    	
    	if (parameter.containsKey("sign-out")){
    		//ログアウト
    		return handleLogOut();
    	} else {
    		//ログイン&表示処理
    		return handleLogin(user_id,password);
    	}
    }
    
    /**
     * ログアウト
     * @return PageResult(ユーザー情報_デフォルト)
     */
    private PageResult handleLogOut() {
    	final Map<String, Object> result = new HashMap<>();
    	result.put("title", "TracEX");
    	PageResult pageResult = PageResult.create(result);
    	pageResult.doLogout(TracExProperties.getString(Name.USER),TracExProperties.getString(Name.USER));
    	return pageResult;
	}

    /**
     * パスワード照合→ログイン
     * @param user_id
     * @param passwd
     * @return PageResult(ユーザー情報)
     */
	private PageResult handleLogin(String user_id, String password){
    	final Map<String, Object> result = new HashMap<>();
    	result.put("title", "TracEX メインページ");
    	PageResult pageResult = PageResult.create(result);
    	
    	UsrTbl usrTbl = dao.getUsrTblBy(user_id, password);
    	
    	if (usrTbl != null){
    		pageResult.authorized(usrTbl.getUser_id(), usrTbl.getName());
    	}
        return pageResult;
    }    
}
