package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource source;

    @Override
    public Author getById(Long id) {
        try (Connection connection = source.getConnection();) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM author WHERE id=" + id);

            if (resultSet.next()) {
                return Author.builder()
                        .id(id)
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
