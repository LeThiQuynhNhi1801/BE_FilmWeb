package film;

import film.api.helper.FileSystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
@Configuration
public class BootifulApplication {

	private static final Logger logger = LoggerFactory.getLogger(BootifulApplication.class);

	public static void main(String[] args) {
//		try {
//			setup();
			SpringApplication.run(BootifulApplication.class, args);
//		} catch (Exception e) {
//			logger.error("An error occurred while starting the application", e);
//		}
	}

	public static void setup() throws IOException {

		// Create static file dir if not exist
		Files.createDirectories(Paths.get(FileSystemHelper.STATIC_FILES_DIR));
		System.out.println("static file dir is created at: " + FileSystemHelper.STATIC_FILES_DIR);
	}
//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurerAdapter() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedOrigins("*");
//			}
//		};
//	}

}
