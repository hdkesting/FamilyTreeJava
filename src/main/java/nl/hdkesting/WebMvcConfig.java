package nl.hdkesting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.ConcurrentHashMap;


@EnableSpringHttpSession
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Bean
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository(new ConcurrentHashMap<>());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

//     /**
//     * Handles favicon.ico requests assuring no <code>404 Not Found</code> error is returned.
//     */
//    @Controller
//    static class FaviconController {
//        @RequestMapping("favicon.ico")
//        String favicon() {
//            return "forward:/resources/static/favicon.ico";
//        }
//    }
}
