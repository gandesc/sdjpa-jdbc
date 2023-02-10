package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {

    Book getById(Long id);

    Book findByTitle(String title);

    Book saveNewBook(Book author);

    Book updateBook(Book author);

    void deleteBookById(Long id);
}
