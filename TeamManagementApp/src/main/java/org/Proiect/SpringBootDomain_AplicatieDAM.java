package org.Proiect;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@SpringBootApplication

public class SpringBootDomain_AplicatieDAM extends SpringBootServletInitializer
{
	private static Logger logger = Logger.getLogger(SpringBootDomain_AplicatieDAM.class.getName());
	
	public static void main(String[] args) {
		logger.info("Loading ... :: SpringBoot - AplicatieDAM Default Settings ...  ");
		SpringApplication.run(SpringBootDomain_AplicatieDAM.class, args);
	}
	@Bean
	public ModelMapper setupModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration()
				.setFieldMatchingEnabled(true)
				.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
				.setSkipNullEnabled(true);

		// Adaugă convertor pentru String la LocalDate
		mapper.addConverter(new AbstractConverter<String, LocalDate>() {
			@Override
			protected LocalDate convert(String source) {
				// Verifică dacă data este null sau dacă are formatul corect
				if (source != null && !source.isEmpty()) {
					try {
						// Folosește un formatter pentru a gestiona data într-un format specific (de exemplu, "yyyy-MM-dd")
						return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					} catch (Exception e) {
						logger.warning("Data invalidă: " + source);
					}
				}
				return null;
			}
		});

		// Adaugă convertor pentru LocalDate la String
		mapper.addConverter(new AbstractConverter<LocalDate, String>() {
			@Override
			protected String convert(LocalDate source) {
				return source != null ? source.toString() : null;
			}
		});

		return mapper;
	}

}