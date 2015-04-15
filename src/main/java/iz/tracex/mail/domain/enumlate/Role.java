package iz.tracex.mail.domain.enumlate;

/**
 * メール受信者のチケット上の役割
 * @author izumikawa_t
 */
public enum Role {
	REPORTER(1,"報告者"),
	DEVELOPER(2,"開発者"),
	EVALUATOR(3,"評価者"),
	CC(4,"関係者");
	
	private final int val;
	private final String role;
	
	private Role(int val, String role){
		this.val = val;
		this.role = role;
	}
	
	public int value(){
		return val;
	}
	
	public String getRole(){
		return role;
	}
	
	public static Role valueOf(int val){
		for (Role r : Role.values()){
			if (r.value() == val){
				return r;
			}
		}
		return null;
	}
}
