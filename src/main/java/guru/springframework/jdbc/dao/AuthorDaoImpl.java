package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource source;

    @Override
    public Author getById(Long id) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM author WHERE id=?");
        ) {
            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Author.builder()
                            .id(id)
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author findByName(String firstName, String lastName) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM author WHERE first_name=? AND last_name=?")
                ) {
                    ps.setString(1, firstName);
                    ps.setString(2, lastName);

                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            return Author.builder()
                                    .id(resultSet.getLong("id"))
                                    .firstName(resultSet.getString("first_name"))
                                    .lastName(resultSet.getString("last_name"))
                                    .build();
                        }
                    }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
