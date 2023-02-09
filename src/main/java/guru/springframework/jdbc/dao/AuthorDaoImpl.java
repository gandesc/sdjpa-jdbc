package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

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
                    return getAuthorFromRS(resultSet);
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
                            return getAuthorFromRS(resultSet);
                        }
                    }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO author (first_name, last_name) VALUES (?, ?)")
                ) {

            ps.setString(1, author.getFirstName());
            ps.setString(2, author.getLastName());
            ps.execute();

            try (ResultSet rs = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()")) {
                if (rs.next()) {
                    Long savedId = rs.getLong(1);

                    return this.getById(savedId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Author getAuthorFromRS(ResultSet resultSet) throws SQLException {
        return Author.builder()
                .id(resultSet.getLong("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .build();
    }
}
