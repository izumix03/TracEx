package iz.tracex.servlet.dispatcher;

import java.util.Map;

/**
 *
 * @author izumi_j
 *
 */
public final class PageResult {
    public final Map<String, Object> values;
    public final String redirectTo;
    private boolean authorized;//新しく認証されたとき
    private String userId;
    private String userName;

    private PageResult(Map<String, Object> values, String redirectTo) {
        super();
        this.values = values;
        this.redirectTo = redirectTo;
    }

    public static PageResult create(Map<String, Object> values) {
        return new PageResult(values, null);
    }

    public static PageResult create(String redirectTo, Map<String, Object> values) {
        return new PageResult(values, redirectTo);
    }

    public String buildRedirectUrl() {
        final StringBuilder result = new StringBuilder(redirectTo).append("?");
        int cnt = 0;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (cnt > 0) {
                result.append("&");
            }
            result.append(entry.getKey()).append("=").append(entry.getValue());
            ++cnt;
        }
        return result.toString();
    }
    
    public boolean isAuthorized(){
    	return authorized;
    }
    
    public void authorized(String userId, String userName){
    	this.authorized = true;
    	this.userId = userId;
    	this.userName = userName;
    }
    
    public void doLogout(String userId, String userName){
    	this.userId = userId;
    	this.userName = userName;
    }
    
    public String getUserId(){
    	return userId;
    }
    
    public String getUserName(){
    	return userName;
    }
}
