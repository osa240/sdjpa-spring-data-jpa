package guru.springframework.jdbc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.AuthorDaoImpl;
import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ActiveProfiles;

/**
 * Created by jt on 8/28/21.
 */
@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@Import({AuthorDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    void testJpaNamed() {
        Book book = bookRepository.jpaNamed("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testBookQueryNative() {
        Book book = bookRepository.findByTitleNativeQuery("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testBookQueryNamed() {
        Book book = bookRepository.findByTitleWithNamedQuery("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testBookQuery() {
        Book book = bookRepository.findByTitleWithQuery("Clean Code");

        assertThat(book).isNotNull();
    }

    @Test
    void testBookFuture() throws ExecutionException, InterruptedException {
        Future<Book> bookFuture = bookRepository.queryByTitle("Clean Code");

        Book book = bookFuture.get();

        assertNotNull(book);
    }

    @Test
    void testBookStream() {
        AtomicInteger count = new AtomicInteger();

        bookRepository.findAllByTitleNotNull().forEach(book -> {
            count.incrementAndGet();
        });

        assertThat(count.get()).isGreaterThan(5);
    }

    @Test
    void testEmptyResultException() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            Book book = bookRepository.readByTitle("foobar4");
        });
    }

    @Test
    void testNullParam() {
        assertNull(bookRepository.getByTitle(null));
    }

    @Test
    void testNoException() {
        assertNull(bookRepository.getByTitle("foo"));
    }
}
