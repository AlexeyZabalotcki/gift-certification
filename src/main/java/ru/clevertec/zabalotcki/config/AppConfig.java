package ru.clevertec.zabalotcki.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.clevertec.zabalotcki.utils.HibernateUtils;

import javax.sql.DataSource;
import java.util.Objects;


@Configuration
@ComponentScan(basePackages = {"ru.clevertec.zabalotcki"})
@PropertySource("classpath:application.yml")
public class AppConfig {

    @Autowired
    DataSourceConfig config;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public DataSource dataSource() {
        System.out.println(config.dataSource());
        return config.dataSource();
    }

    @Bean
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource())
                .locations("classpath:db/migration")
                .load();

        Objects.requireNonNull(flyway).migrate();
        return flyway;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.sessionFactory();
    }

//    @Bean
//    public GiftCertificateRepositoryImpl certificateRepository() {
//        return new GiftCertificateRepositoryImpl(sessionFactory());
//    }
//
//    @Bean
//    public TagRepositoryImpl tagRepository() {
//        return new TagRepositoryImpl(sessionFactory());
//    }
//
//    @Bean
//    public GiftCertificateMapper certificateMapper() {
//        return new GiftCertificateMapper();
//    }
//
//    @Bean
//    public TagMapper tagMapper() {
//        return new TagMapper();
//    }
//
//    @Bean
//    public GiftCertificateServiceImpl certificateService(GiftCertificateRepositoryImpl giftCertificateRepository, GiftCertificateMapper giftCertificateMapper) {
//        return new GiftCertificateServiceImpl(giftCertificateRepository, giftCertificateMapper);
//    }
//
//    @Bean
//    public TagServiceImpl tagService(TagRepositoryImpl tagRepository, TagMapper mapper) {
//        return new TagServiceImpl(tagRepository, mapper);
//    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(mapper);
        return converter;
    }

//    @Bean
//    public GiftCertificateController certificateController(GiftCertificateServiceImpl certificateService, MappingJackson2HttpMessageConverter converter) {
//        return new GiftCertificateController(certificateService, converter);
//    }
//
//    @Bean
//    public TagController tagController(TagServiceImpl tagService, MappingJackson2HttpMessageConverter converter) {
//        return new TagController(tagService, converter);
//    }
}
