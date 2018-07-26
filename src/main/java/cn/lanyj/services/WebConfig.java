package cn.lanyj.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Raysmond .
 */
@Configuration
@EnableWebMvc
@EnableScheduling
public class WebConfig implements WebMvcConfigurer {
    private static final String[] RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

	private Logger log = LoggerFactory.getLogger(WebConfig.class);
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    	MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//    	ObjectMapper mapper = new ObjectMapper();
//    	mapper.registerModule(new Hibernate5Module());
//    	jsonConverter.setObjectMapper(mapper);
    	converters.add(jsonConverter);
    }
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    	configurer.defaultContentType(MediaType.ALL);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.debug("Register CORS configuration");
        registry
        		.addMapping("/open/api/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        try {
//            File directiory = new File("");
//            log.info("Resource path: " + directiory.getCanonicalPath());
//            registry
//	            .addResourceHandler("/**")
//	            .addResourceLocations(RESOURCE_LOCATIONS)
//	            .addResourceLocations("file:" + directiory.getCanonicalPath() + "/");
//        } catch (Exception e) {
//        }
        registry
        .addResourceHandler("/**")
        .addResourceLocations(RESOURCE_LOCATIONS);
    }
    
}
