package peaksoft.services;

import peaksoft.entities.Book;
import peaksoft.repositories.BookRepo;
import peaksoft.repositories.BookRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BookServiceImpl implements BookService{
    BookRepo bookRepo=new BookRepository();

    @Override
    public String saveBook(Book book) {
        bookRepo.save(book);
        return "Successfully saved book: "+book.toString();
    }

    @Override
    public String cleanTableBook() {
        bookRepo.cleanBook();
        return "Table book is successfully cleaned!";
    }

    @Override
    public void saveAllBooks(List<Book> books) {
        bookRepo.saveAll(books);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepo.getAll();
    }

    @Override
    public Book findById(Long bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Not found book with id = " + bookId));

        return book;
    }

    @Override
    public String deleteBookById(Long bookId) {
        bookRepo.deleteById(bookId);
        return "Book with id = "+bookId+" is successfully deleted!";
    }

    @Override
    public String updateBookById(Long id, Book book) {
        bookRepo.updateBook(id,book);
        return "Book with id = "+id+" is successfully updated! ";
    }

    @Override
    public Map<String, List<Book>> getBookByAuthor(String authorName) {
        return bookRepo.getBookByAuthor(authorName);
    }

    @Override
    public List<Book> sortByPrice(String ascOrDesc) {
        return bookRepo.sortByPrice(ascOrDesc);
    }

    @Override
    public Map<String, BigDecimal> avgPriceOfAuthorBooks(String authorName) {
        return bookRepo.avgPriceOfAuthorBooks(authorName);
    }


}
