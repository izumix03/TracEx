package iz.tracex.mail.domain;

/**
 * メール送信先別内容
 * 
 * @author works780
 */
public class MailInfo {
	private final ReceiverInfo	receiver;
	private final String	   subject;
	private final String	   body;
	
	public ReceiverInfo getReceiver() {
		return receiver;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public static interface ReceiversBuilder {
		SubjectBuilder receiver(ReceiverInfo receiver);
	}
	
	public static interface SubjectBuilder {
		BodyBuilder subject(String subject);
	}
	
	public static interface BodyBuilder {
		Builder body(String body);
	}
	
	public static class Builder implements ReceiversBuilder, SubjectBuilder, BodyBuilder {
		private ReceiverInfo	receiver;
		private String		 subject;
		private String		 body;
		
		public Builder() {
		}
		
		public SubjectBuilder receiver(ReceiverInfo receiver) {
			this.receiver = receiver;
			return this;
		}
		
		public BodyBuilder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		public Builder body(String body) {
			this.body = body;
			return this;
		}
		
		public MailInfo build() {
			//不要
			if (receiver == null || subject == null || body == null) {
				throw new NullPointerException();
			}
			return new MailInfo(this);
		}
	}
	
	private MailInfo(Builder builder) {
		this.receiver = builder.receiver;
		this.subject = builder.subject;
		this.body = builder.body;
	}
}
