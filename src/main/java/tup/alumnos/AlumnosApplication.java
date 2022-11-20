package tup.alumnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/*Anotación que le dice a Spring que el proyecto va a ser una aplicación de SpringBoot, además activa 3
herramientas de configuración @EnableConfiguration,  @ComponentScan y @SpringBootConfiguration. */
@SpringBootApplication
public class AlumnosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumnosApplication.class, args);
	}
}