package iz.tracex.dao;

import iz.tracex.dto.trac.UsrTbl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * ログイン及びユーザー情報の取得
 * @author izumikawa_t
 *
 */
public final class UsrTblDao extends AbstractDao<UsrTbl> {
	private static final String LOGIN_SQL = "SELECT * FROM usrtbl WHERE user_id = ? AND password = password(?)";
	private static final String GET_USERS_BY_ID_BASE = "SELECT * FROM usrtbl WHERE user_id in (";

	/**
	 * login時ユーザー情報取得
	 */
    public UsrTbl getUsrTblBy(String user_id, String password) {
    	List<UsrTbl> usrTblList = query(LOGIN_SQL, user_id, password);
    	login(user_id);
    	return usrTblList.size() != 0 ? usrTblList.get(0):null;
	}
    
    /**
     * ユーザー情報
     * @param user_id
     * @return
     */
	public UsrTbl getUserTblInfo(String user_id){
		return queryForObject(new String[] {"user_id"}, user_id);
	}
	
	/**
	 * メール送信用(のみ？)ユーザーリストをユーザーIDのリストから返す
	 */
	public List<UsrTbl> getUsrInfoList(String[] ids){
		if (ids.length == 0){
			return Collections.emptyList();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i< ids.length;i++){
			if (i ==0){
				sb.append(GET_USERS_BY_ID_BASE).append("?");
			}else{
				sb.append(",?");
			}
		}
		sb.append(")");
		return query(sb.toString(), (Object[])ids);
	}
	
    
    /**
     * 使ったかどうかをupd
     * @param user_id
     * @param passwd
     */
    private void login(String user_id){
    	update(new String[] {"receive_mail"}, new Object[] {1} , new String[]{"user_id"}, user_id);
    }

	@Override
    protected Class<UsrTbl> entityClass() {
        return UsrTbl.class;
    }
	
	protected class UsrTblProcessor implements DtoProcessor<UsrTbl>{
		@Override
		public UsrTbl toDto(ResultSet rs) throws SQLException {
			DtoMapper<UsrTbl>  dtoMapper = new DtoMapper<UsrTbl>();
			return dtoMapper.toDto(rs);
		}
	}

	public void updatedBeforeRead(long resultId, String userId) {
		update(new String[] {"updated_ticket_id", "updated_time"}, new Object[] {resultId,(System.currentTimeMillis() / 1000L)}, new String[] {"user_id"}, userId);
	}

	public int getUpdatedTicketTime(long id, String userId) {
		UsrTbl user = getUserTblInfo(userId);
		if (user.getUpdated_ticket_id() == id){
			return user.getUpdated_time();
		}
		return 0;
	}

	public void updateAfterRead(String userId) {
		update(new String[] {"updated_ticket_id", "updated_time"}, new Object[] {"0","0"}, new String[] {"user_id"}, userId);
	}
	
}

