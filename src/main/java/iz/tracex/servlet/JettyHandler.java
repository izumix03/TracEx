package iz.tracex.servlet;

import iz.tracex.base.TracExProperties;
import iz.tracex.base.TracExProperties.Name;
import iz.tracex.base.jdbc.ConnectionManager;
import iz.tracex.dao.UserDao;
import iz.tracex.dto.trac.UsrTbl;
import iz.tracex.dto.trac.translate.User;
import iz.tracex.servlet.dispatcher.AjaxDispatcher;
import iz.tracex.servlet.dispatcher.PageDispatcher;
import iz.tracex.servlet.dispatcher.PageResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author izumi_j
 * 
 */
public final class JettyHandler extends AbstractHandler {
	private static final Logger	  logger	      = LoggerFactory.getLogger(JettyHandler.class);
	
	private static final String	  CHARSET	      = "UTF-8";
	private static final String	  INDEX_URL	      = "/index";
	private static final String	  LIST_URL	      = "/list";
	private static final String	  REQ_AJAX	      = "ajax";
	private static final String[]	REQ_RESOURCES	= new String[] { "css", "img", "js", "lib" };
	
	private TemplateEngine	      templateEngine;
	private ObjectMapper	      objectMapper;
	
	private UserDao	              memberDao;
	
	@Override
	protected void doStart() throws Exception {
		// Thymeleaf
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setPrefix("html/");
		templateResolver.setSuffix(".html");
		templateResolver.setCharacterEncoding(CHARSET);
		templateResolver.setCacheTTLMs(3600000L);
		templateResolver.setCacheable(!TracExProperties.getBoolean(Name.DEVELOP));
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
		// JSON
		objectMapper = new ObjectMapper();
		
		// etc
		memberDao = new UserDao();
		
		// sysdate
		SystemTimeInitializer.initialize();
		
		super.doStart();
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException,
	        ServletException {
		
		final String requestUri = baseRequest.getRequestURI();
		logger.trace("Request uri = {}", requestUri);
		logger.trace("Request parameter = {}", request.getParameterMap());
		
		final String uriFirst = getFirstHierarchyOf(requestUri);
		final String uriSecond = getSecondaryHierarchyOf(requestUri);
		
		try {
			
			response.setCharacterEncoding(CHARSET);
			
			if (StringUtils.equalsIgnoreCase(uriFirst, REQ_AJAX)) {
				// 　Ajaxリクエスト
				logger.debug("Ajax process name = {}", uriSecond);
				final AjaxDispatcher ajax = AjaxDispatcher.of(uriSecond);
				if (ajax == null || StringUtils.isEmpty(uriSecond)) {
					logger.error("Unknown request! uri = {}", request);
					throw new IllegalArgumentException("Unknown request!");
				}
				
				// session管理
				HttpSession session = request.getSession();
				String userId = (String) session.getAttribute("userId");
				
				// ユーザー設定
				if (userId == null) {
					userId = TracExProperties.getString(Name.USER);
				}
				
				response.setContentType("application/json");
				Object obj = ajax.dispatch(request, userId);
				
				// ログインAjax
				if (obj != null && obj instanceof UsrTbl) {
					Cookie cookie = new Cookie("JSESSIONID", session.getId());
					cookie.setMaxAge(864000);// Cookie有効期限10日
					cookie.setPath("/");
					response.addCookie(cookie);
					
					session.setAttribute("userId", ((UsrTbl) obj).getUser_id());
					session.setAttribute("userName", ((UsrTbl) obj).getName());
				}
				
				objectMapper.writeValue(response.getWriter(), obj);
				
			} else if (ArrayUtils.contains(REQ_RESOURCES, uriFirst)) {
				// 　静的リソース取得リクエスト
				final WebResource res = WebResources.get(StringUtils.removeStart(requestUri, "/"));
				response.setContentType(res.contentType);
				response.getOutputStream().write(res.content);
				
			} else {
				// ページ表示リクエスト
				logger.debug("Page name = {}", uriFirst);
				final PageDispatcher page = PageDispatcher.of(uriFirst);
				
				// session管理
				HttpSession session = request.getSession();
				String userId = (String) session.getAttribute("userId");
				String userName = (String) session.getAttribute("userName");
				
				// ユーザー設定
				if (userId == null) {
					userId = TracExProperties.getString(Name.USER);
					userName = getUserName();
				}
				
				// URI不正
				if (page == null || StringUtils.isNotEmpty(uriSecond)) {
					logger.warn("Invalid uri, so redirect to index.");
					response.sendRedirect(INDEX_URL);
					return;
				}
				
				// メイン処理
				response.setContentType("text/html");
				final PageResult result = page.dispatch(request, userId);
				
				// 処理結果、ログインまたはログアウトされた場合
				if (result.getUserName() != null) {
					Cookie cookie = new Cookie("JSESSIONID", session.getId());
					cookie.setMaxAge(864000);// Cookie有効期限10日
					cookie.setPath("/");
					response.addCookie(cookie);
					
					session.setAttribute("userId", result.getUserId());
					session.setAttribute("userName", result.getUserName());
					String redirectTo = result.isAuthorized() ? LIST_URL : INDEX_URL;
					response.sendRedirect(redirectTo);
				} else if (StringUtils.isEmpty(result.redirectTo)) {
					// 通常表示
					final Context ctx = new Context();
					ctx.setVariable("css", page.identifier());
					ctx.setVariable("userId", userId);
					ctx.setVariable("userName", userName);
					ctx.setVariables(result.values);
					templateEngine.process(page.identifier(), ctx, response.getWriter());
				} else {
					// 処理後リダイレクト(更新後など)
					response.sendRedirect(result.buildRedirectUrl());
				}
			}
			
			ConnectionManager.commitAndClose();
			
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
		} catch (Throwable e) {
			logger.error("Error occurred!", e);
			ConnectionManager.rollbackAndClose();
			throw e;
		}
	}
	
	private String getFirstHierarchyOf(String requestUri) {
		final String[] parts = StringUtils.split(requestUri, "/");
		if (parts.length > 0) {
			return parts[0];
		} else {
			return null;
		}
	}
	
	private String getSecondaryHierarchyOf(String requestUri) {
		final String[] parts = StringUtils.split(requestUri, "/");
		if (parts.length > 1) {
			return parts[1];
		} else {
			return null;
		}
	}
	
	private String getUserName() {
		final User m = memberDao.selectBy(TracExProperties.getString(Name.USER));
		return m != null ? m.getName() : null;
	}
}
