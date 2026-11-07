package co.edu.demoacademico.config;

import co.edu.demoacademico.model.Estudiante;
import co.edu.demoacademico.repository.EstudianteRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataSeeder {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.cantidad:500}")
    private int cantidad;

    @Bean
    CommandLineRunner seedData(EstudianteRepository repository) {
        return args -> {
            if (!enabled) return;
            if (repository.count() > 0) return;

            Faker faker = new Faker(new Locale("es"));

            for (int i = 0; i < cantidad; i++) {
                Estudiante e = new Estudiante();
                e.setNombre(faker.name().firstName());
                e.setApellido(faker.name().lastName());
                // el índice hace que el email sea único sin reintentos
                e.setEmail("estudiante" + i + "@demoacademico.edu");
                e.setEdad(faker.number().numberBetween(16, 31));
                repository.save(e);
            }
        };
    }
}
