package populo.controller;

import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The controller interface of our MVC web app. We are going framework-less for
 * a bit of learning experience and some back to basics programming.
 *
 * @author amv
 */
public interface Controller {

	/**
	 * Typical interface used in MVC webapp. The controller in our application
	 * gets a reference to the Thymeleaf Template Engine in the main 'handle'
	 * method in addition to the Servlet Spec Objects.
	 *
	 * @param request
	 * @param response
	 * @param servletContext
	 * @param templateEngine
	 * @throws IOException
	 */
	public void handle(
			HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext, TemplateEngine templateEngine)
			throws IOException;

}
