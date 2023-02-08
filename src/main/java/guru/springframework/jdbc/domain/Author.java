package guru.springframework.jdbc.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Author {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
}
