package iz.tracex.mail.base;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public final class MailSessionCreator {
	public static final Properties	props;
	static {
		props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.securemx.jp");
		props.setProperty("mail.smtp.port", "587");
		props.setProperty("mail.smtp.auth", "true");
		
		// タイムアウト
		props.setProperty("mail.smtp.connectiontimeout", "8000");
		props.setProperty("mail.smtp.timeout", "8000");
		
		props.setProperty("mail.user", "izumikawa_t%worksap.co.jp");
		props.setProperty("mail.host", "smtp.securemx.jp");
		
		// props.setProperty("mail.debug", "true");
	}
	
	public static Session createSession() {
		class PasswordAuthenticatior extends Authenticator {
			private String	userName;
			private String	password;
			
			PasswordAuthenticatior(String userName, String password) {
				this.userName = userName;
				this.password = password;
			}
			
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		}
		// パスワード認証つきのセッションを作成
		return Session.getDefaultInstance(props, new PasswordAuthenticatior("izumikawa_t%worksap.co.jp", "IzumgZpz"));
	}
	
	
}
