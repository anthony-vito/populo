package populo.controller;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author amv
 */
public class HomeController implements Controller {

	public void handle(HttpServletRequest request, HttpServletResponse response,
					   ServletContext servletContext, TemplateEngine templateEngine) throws IOException {

		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process("home", ctx, response.getWriter());
	}

}
