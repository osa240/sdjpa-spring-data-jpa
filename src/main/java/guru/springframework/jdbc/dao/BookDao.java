package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;

public interface BookDao {
    Book saveNewBook(Book book);

    void deleteBookById(Long id);

    Book getById(Long id);

    void updateBook(Book saved);

    Book findBookByTitle(String title);
}
