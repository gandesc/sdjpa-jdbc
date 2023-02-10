package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    void testGetBook() {
        Book book = bookDao.getById(1L);

        assertThat(book).isNotNull();
    }

    @Test
    void testGetBookByName() {
        Book book = bookDao.findByTitle("Spring in Action, 5th Edition");

        assertThat(book).isNotNull();
    }

    @Test
    void testSaveNewBook() {
        Book book = Book.builder()
                .title("John")
                .isbn("123")
                .publisher("Foo")
                .build();

        Book savedEntity = bookDao.saveNewBook(book);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getTitle()).isEqualTo(savedEntity.getTitle());
    }

    @Test
    void testUpdateExistingBook() {
        Book book = Book.builder()
                .title("One Title")
                .isbn("123")
                .build();

        Book savedEntity = bookDao.saveNewBook(book);

        savedEntity.setTitle("Another Title");

        Book updatedEntity = bookDao.updateBook(savedEntity);

        assertThat(updatedEntity.getTitle()).isEqualTo(savedEntity.getTitle());
    }

    @Test
    void testDeleteBook() {
        Book book = Book.builder()
                .title("John")
                .isbn("123")
                .build();

        Book savedEntity = bookDao.saveNewBook(book);

        assertThat(savedEntity).isNotNull();

        bookDao.deleteBookById(savedEntity.getId());

        assertThat(bookDao.getById(savedEntity.getId())).isNull();
    }
}
