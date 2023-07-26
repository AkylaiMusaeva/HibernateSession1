package peaksoft.services;

import peaksoft.entities.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BookService {
    String saveBook(Book book);

    void saveAllBooks(List<Book> books);

    List<Book> getAllBooks();

    Book findById(Long bookId);

    String  deleteBookById(Long bookId);

    String updateBookById(Long id, Book book);

    Map<String,List<Book>> getBookByAuthor(String authorName);

    List<Book> sortByPrice(String ascOrDesc);

    Map<String, BigDecimal> avgPriceOfAuthorBooks(String authorName);
    String cleanTableBook();
}
