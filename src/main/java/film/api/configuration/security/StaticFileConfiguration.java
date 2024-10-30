package film.api.configuration.security;

import film.api.helper.FileSystemHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class StaticFileConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File staticDir = new File(FileSystemHelper.STATIC_FILES_DIR);
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations(staticDir.toURI().toString())
                .setCacheControl(CacheControl.noCache());
    }
}
