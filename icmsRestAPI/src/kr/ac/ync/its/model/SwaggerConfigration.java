package kr.ac.ync.its.model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerConfigration extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException{
		super.init(config);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setTitle("Its Swagger Demo");
		beanConfig.setHost("localhost:50080");
		beanConfig.setBasePath("http://.....:50080");
		beanConfig.setBasePath("/RestAPI/apis");
		beanConfig.setResourcePackage("kr.ac.ync.its");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
		beanConfig.setSchemes(new String[] {"http"});
		beanConfig.setVersion("1.0");
	}

}
