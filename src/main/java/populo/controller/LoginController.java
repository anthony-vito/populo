package populo.controller;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The login form submits directly back to the login page, but the Shiro
 * filter will perform authentication and redirect the request as configured.
 * Thus, the login controller here merely needs to send out the initial
 * HTML form itself.
 *
 * @author amv
 */
public class LoginController implements Controller {

	public void handle(HttpServletRequest request, HttpServletResponse response,
										 ServletContext servletContext, TemplateEngine templateEngine) throws IOException {

		WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());

		templateEngine.process("login", ctx, response.getWriter());

	}

}
