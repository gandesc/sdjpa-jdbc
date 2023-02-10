package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class BookDaoImpl implements BookDao {

    private final DataSource source;

    @Override
    public Book getById(Long id) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM book WHERE id=?");
        ) {
            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getBookFromRS(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Book findByTitle (String title) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM book WHERE title=?")
        ) {
            ps.setString(1, title);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getBookFromRS(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Book saveNewBook(Book book) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO book (title, isbn, publisher) VALUES (?, ?, ?)")
        ) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getPublisher());
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

    @Override
    public Book updateBook(Book book) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("UPDATE book SET title=?, isbn=?, publisher=? WHERE id=?")
        ) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getIsbn());
            ps.setString(3, book.getPublisher());
            ps.setLong(4, book.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this.getById(book.getId());
    }

    @Override
    public void deleteBookById(Long id) {
        try (
                Connection connection = source.getConnection();
                PreparedStatement ps = connection.prepareStatement("DELETE FROM book WHERE id=?");
        ) {
            ps.setLong(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Book getBookFromRS(ResultSet resultSet) throws SQLException {
        return Book.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .isbn(resultSet.getString("isbn"))
                .publisher(resultSet.getString("publisher"))
                .build();
    }
}
