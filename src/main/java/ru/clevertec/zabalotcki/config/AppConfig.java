package ru.clevertec.zabalotcki.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.clevertec.zabalotcki.controller.GiftCertificateController;
import ru.clevertec.zabalotcki.controller.TagController;
import ru.clevertec.zabalotcki.dao.GiftCertificateRepositoryImpl;
import ru.clevertec.zabalotcki.dao.TagRepositoryImpl;
import ru.clevertec.zabalotcki.service.GiftCertificateMapper;
import ru.clevertec.zabalotcki.service.GiftCertificateServiceImpl;
import ru.clevertec.zabalotcki.service.TagMapper;
import ru.clevertec.zabalotcki.service.TagServiceImpl;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@PropertySource("classpath:application.yml")
public class AppConfig implements WebMvcConfigurer {


    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/gifts");
        dataSource.setUsername("gifts");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public Flyway flyway() {
        Flyway flyway = null;
        try {
            flyway = Flyway.configure()
                    .dataSource(dataSource())
                    .locations("classpath:db/migration")
                    .load();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(flyway).migrate();
        return flyway;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TagRepositoryImpl tagRepository(JdbcTemplate jdbcTemplate) {
        return new TagRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public GiftCertificateRepositoryImpl certificateRepository(JdbcTemplate jdbcTemplate) {
        return new GiftCertificateRepositoryImpl(jdbcTemplate, tagRepository(jdbcTemplate));
    }

    @Bean
    public GiftCertificateMapper certificateMapper() {
        return new GiftCertificateMapper();
    }

    @Bean
    public TagMapper tagMapper() {
        return new TagMapper();
    }

    @Bean
    public GiftCertificateServiceImpl certificateService(GiftCertificateRepositoryImpl repository, GiftCertificateMapper mapper) {
        return new GiftCertificateServiceImpl(repository, mapper);
    }

    @Bean
    public TagServiceImpl tagService(TagRepositoryImpl repository, TagMapper mapper) {
        return new TagServiceImpl(repository, mapper);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    public GiftCertificateController certificateController(GiftCertificateServiceImpl service, MappingJackson2HttpMessageConverter converter) {
        return new GiftCertificateController(service, converter);
    }

    @Bean
    public TagController tagController(TagServiceImpl service, MappingJackson2HttpMessageConverter converter) {
        return new TagController(service, converter);
    }
}
