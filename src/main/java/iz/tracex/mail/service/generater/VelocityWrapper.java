package iz.tracex.mail.service.generater;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * velocityを使うためのラッパークラス
 * @author works780
 */
public class VelocityWrapper {
	private Template	    template	= null;
	private VelocityContext	context	 = new VelocityContext();
	private VelocityEngine	engine	 = new VelocityEngine();
	
	/**
	 * コンストラクタ
	 * @param templateFileName
	 * @throws IOException
	 * @throws Exception
	 */
	public VelocityWrapper(String templateFileName) throws IOException, Exception {
		// velocity.propertiesによるVelocityEngineの初期化
		Properties prop = new Properties();
		//本来は設定ファイルを読もう
		prop.setProperty("file.resource.loader.path", "C:/skillUp/TracEx/bin/mail");
		prop.setProperty("input.encoding", "utf-8");
		engine.init(prop);

		template = engine.getTemplate( templateFileName);
	}
	
	/**
	 * 変数定義用
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value) {
		context.put(key, value);
	}
	
	/**
	 * 結果取得用
	 * @return
	 * @throws ResourceNotFoundException
	 * @throws ParseErrorException
	 * @throws MethodInvocationException
	 * @throws Exception
	 */
	public String merge() throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, Exception {
		StringWriter sw = new StringWriter();
		template.merge(context, sw);
		return sw.toString();
	}
}
