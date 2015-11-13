package populo;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import populo.controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple, Non-Framework, Dispatcher Servlet.
 *
 * Concepts and snippets borrowed from Thymeleaf documentation.
 *
 * @author amv
 */
public class Dispatcher extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

	private static TemplateEngine templateEngine;

	static {
		initializeTemplateEngine();
	}

	private static void initializeTemplateEngine() {

		ServletContextTemplateResolver templateResolver =
				new ServletContextTemplateResolver();
		// XHTML is the default mode, but we set it anyway for better understanding of code
		templateResolver.setTemplateMode("XHTML");
		// This will convert "home" to "/WEB-INF/templates/home.html"
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		// Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
		templateResolver.setCacheTTLMs(3600000L);

		// TODO: Dynamically Set This For Local vs Production
		templateResolver.setCacheable(false);

		templateEngine = new TemplateEngine();
		templateEngine.addDialect(new ShiroDialect());
		templateEngine.setTemplateResolver(templateResolver);

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {

		try {

			log.debug(request.getRequestURI());

			/*
			 * Query controller/URL mapping and obtain the controller
			 * that will process the request. If no controller is available,
			 * return false and let other filters/servlets process the request.
			 */
			Controller controller = Application.resolveControllerForRequest(request);
			if (controller == null) {
				getServletContext().getNamedDispatcher("default").forward(request, response);
				return;
			}
			/*
			 * Write the response headers
			 */
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);

			/*
			 * Execute the controller and process view template,
			 * writing the results to the response writer.
			 */
			controller.handle(request, response, this.getServletContext(), templateEngine);

		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

}
