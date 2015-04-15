package iz.tracex.mail;

@SuppressWarnings("serial")
public class MailSenderException extends Exception {
	/**
	 * コンストラクタ
	 */
	public MailSenderException(){
		super();
	}
	
	/**
	 * メッセージ付け以上
	 */
	public MailSenderException(String msg){
		super(msg);
	}
	
	public MailSenderException(String msg, Throwable throwable){
		super(msg, throwable);
	}
}
