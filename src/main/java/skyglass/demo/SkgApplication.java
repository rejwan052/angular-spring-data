package skyglass.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@ComponentScan(basePackages = {
		"skyglass.demo.config",
		"skyglass.demo.rest",
		"skyglass.demo.service",
		"skyglass.data.rest", 
		"skyglass.data.service"})
@EnableJpaRepositories(basePackages = {"skyglass.data.data", "skyglass.demo.data"})
@EntityScan(basePackages = {"skyglass.data.model", "skyglass.demo.model"})
public class SkgApplication {
	
	public static final String APP_NAME = "skyglass";
	
		// Match everything without a suffix (so not a static resource)
		@RequestMapping(value = "/" + APP_NAME + "{[path:[^\\.]*}")
		public String redirectApp() {
			// Forward to home page so that route is preserved.
			return "forward:/";
		}	

		// Match everything without a suffix (so not a static resource)
		@RequestMapping(value = "{[^(" + APP_NAME + ")]+[path:[^\\.]*}")
		public String redirect() {
			// Forward to home page so that route is preserved.
			return "forward:/";
		}	
		
		@RequestMapping(value = "/" + APP_NAME + "/**")
		public String redirectBookmarkableUrlApp() {
			// Forward to home page so that route is preserved.
			return "forward:/";
		}
		
		public static void main(String[] args) {
			SpringApplication.run(SkgApplication.class, args);
		}
}
