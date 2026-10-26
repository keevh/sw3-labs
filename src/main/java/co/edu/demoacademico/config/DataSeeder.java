package co.edu.demoacademico.config;

import co.edu.demoacademico.estudiantes.model.Estudiante;
import co.edu.demoacademico.estudiantes.repository.EstudianteRepository;
import com.github.javafaker.Faker;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Seeder de Estudiantes (Lab 02). Ahora lee app.seed.cantidad-estudiantes. */
@Configuration
public class DataSeeder {

    @Value("${app.seed.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.cantidad-estudiantes:100}")
    private int cantidad;

    @Bean
    CommandLineRunner seedEstudiantes(EstudianteRepository repo) {
        return args -> {
            if (!enabled || cantidad <= 0) return;
            if (repo.count() > 0) return;

            Faker faker = new Faker(new Locale("es"));
            int creados = 0;
            int intentos = 0;

            while (creados < cantidad && intentos < cantidad * 3) {
                intentos++;
                String nombre = faker.name().fullName();
                String email = ("est" + creados + "_" + faker.internet().emailAddress())
                        .toLowerCase()
                        .replace(" ", "")
                        .replace("..", ".");

                Estudiante e = new Estudiante();
                e.setNombre(nombre);
                e.setEmail(email);

                try {
                    repo.save(e);
                    creados++;
                } catch (Exception ex) {
                    // si el email es único y falla, reintenta
                }
            }
        };
    }
}
