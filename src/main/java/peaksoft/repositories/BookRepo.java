package peaksoft.repositories;

import peaksoft.entities.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookRepo {


    void save(Book book);

    void saveAll(List<Book> books);

    List<Book> getAll();

    Optional<Book>findById(Long bookId);


    void deleteById(Long bookId);

    void updateBook(Long id, Book book);

    Map<String, List<Book>> getBookByAuthor(String authorName);

    List<Book> sortByPrice(String ascOrDesc);

    Map<String, BigDecimal> avgPriceOfAuthorBooks(String authorName);

    void cleanBook();
}
