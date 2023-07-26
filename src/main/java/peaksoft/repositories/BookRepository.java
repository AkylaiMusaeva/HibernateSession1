package peaksoft.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import peaksoft.configuration.DatabaseConnection;
import peaksoft.entities.Book;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.*;

public class BookRepository implements BookRepo, AutoCloseable {
    private final EntityManagerFactory entityManagerFactory = DatabaseConnection.createEntityManagerFactory();
    private final SessionFactory sessionFactory = DatabaseConnection.createSessionFactory();

    @Override
    public void save(Book book) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(book);

        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public void cleanBook() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        //Version 1
        entityManager.createQuery("""
                delete from Book b
                """).executeUpdate();

        //Version 2
//        entityManager.createNativeQuery("truncate table books").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void saveAll(List<Book> books) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        books.forEach(book -> entityManager.persist(book));
        System.out.println("List of books is successfully saved!");
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public List<Book> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Book> books = entityManager.createQuery("""
                select b from Book b
                """, Book.class).getResultList();

        //Version 2(не получилось)
//        EntityManager em = entityManagerFactory.createEntityManager();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
//        Root<Book> rootEntry = cq.from(Book.class);
//        CriteriaQuery<Book> all = cq.select(rootEntry);
//        TypedQuery<Book> allQuery = em.createQuery(all);
//        return allQuery.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return books;

    }

    @Override
    public Optional<Book> findById(Long bookId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        //Version 1
        Book book = entityManager.find(Book.class, bookId);//обычный способ,но он возвращает null если нет книги которую просит пользователь

        //Version 2
//        Book book = entityManager.createQuery("""
//                select b from Book b where b.id=:newId
//                """, Book.class).setParameter(1, bookId).getSingleResult();//второй метод,возвращает NoResultException если книги нет в бд

        entityManager.getTransaction().commit();
        entityManager.close();
        return Optional.ofNullable(book);
    }

    @Override
    public void deleteById(Long bookId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        //Version 1
//        int id = entityManager.createQuery("""
//                delete from Book b where b.id=:id
//                """).setParameter("id", bookId).executeUpdate();

        //Version 2
        Book book = entityManager.find(Book.class, bookId);
        entityManager.remove(book);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void updateBook(Long id, Book book) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        //Version 1
//        Book book1 = entityManager.find(Book.class, id);
//        book1.setName(book.getName());
//        book1.setAuthor(book.getAuthor());
//        book1.setPrice(book.getPrice());

        //Version 2
//        entityManager.createQuery("update Book b set name=:newName,author=:newAuthor,price=:newPrice where b.id=:newId")
//                .setParameter("newName", book.getName())
//                .setParameter("newAuthor", book.getAuthor())
//                .setParameter("newPrice", book.getPrice())
//                        .setParameter("newId",id).executeUpdate();

        //Version 3
        Book currentBook=entityManager.find(Book.class,id);
        currentBook.setName(book.getName());
        currentBook.setAuthor(book.getAuthor());
        currentBook.setPrice(book.getPrice());
        entityManager.merge(currentBook);
        Book book1 = entityManager.createQuery("select b from  Book b where name=?1 and author=?2 and price=?3", Book.class)
                .setParameter(1, book.getName())
                .setParameter(2, book.getAuthor())
                .setParameter(3, book.getPrice())
                .getSingleResult();


        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public Map<String, List<Book>> getBookByAuthor(String authorName) {
        Map<String, List<Book>> map = new HashMap<>();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Book> booksByAuthorName = entityManager.createQuery("""
                select b from Book b where b.author ilike :newName
                """, Book.class).setParameter("newName", "%" + authorName + "%").getResultList();
        map.put(authorName, booksByAuthorName);
        entityManager.getTransaction().commit();
        entityManager.close();
        return map;
    }

    @Override
    public List<Book> sortByPrice(String ascOrDesc) {
        List<Book> books = new ArrayList<>();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Book> books1 = entityManager.createQuery("""
                select b from Book b order by b.price  
                """ + ascOrDesc, Book.class).getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return books1;
    }

    @Override
    public Map<String, BigDecimal> avgPriceOfAuthorBooks(String authorName) {
        Map<String, BigDecimal> map = new HashMap<>();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Double avgPrice = (Double) entityManager.createQuery("""
                select round(avg(b.price),2) from Book b where b.author ilike :newAuthorName
                """).setParameter("newAuthorName", "%" + authorName + "%").getSingleResult();
        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println("Average price of book's by author - " + authorName);
        map.put(authorName, BigDecimal.valueOf(avgPrice));
        return map;
    }


    @Override
    public void close() throws Exception {
        entityManagerFactory.close();
        sessionFactory.close();
    }
}
