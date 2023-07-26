package peaksoft;

import peaksoft.configuration.DatabaseConnection;
import peaksoft.entities.Book;
import peaksoft.services.BookService;
import peaksoft.services.BookServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DatabaseConnection.createEntityManagerFactory();
        BookService bookService=new BookServiceImpl();
        Scanner scanWord=new Scanner(System.in);
        Scanner scanNum=new Scanner(System.in);
        while(true){
            System.out.println("""
                    1-save book
                    2-save list of books
                    3-get all books
                    4-find book by id 
                    5-delete book by id 
                    6-update book by id 
                    7-get books by author name
                    8-sort by price
                    9-find average price of one author's books
                    10-clean table
                    """);
            switch (new Scanner(System.in).nextLine()){
                case "1"->{
                    System.out.println("Input book name: ");
                    String name=scanWord.nextLine();
                    System.out.println("Input book author: ");
                    String author=scanWord.nextLine();
                    System.out.println("Input book price: ");
                    int price=scanNum.nextInt();
                    System.out.println(bookService.saveBook(
                            new Book(name, author, BigDecimal.valueOf(price))));
                }
                case "2"-> {
                    bookService.saveAllBooks(
                            List.of(
                                    new Book("Listen to Your Heart: The London Adventure", "Ruskin Bond", BigDecimal.valueOf(1200)),
                                    new Book("THE PLIGHT OF A POSTMODERN HUNTER", "Chyngyz Aitmatov", BigDecimal.valueOf(1900)),
                                    new Book("Jamilia", "Chyngyz Aitmatov", BigDecimal.valueOf(3200)),
                                    new Book("The White Ship", "Chyngyz Aitmatov", BigDecimal.valueOf(5000)),
                                    new Book("Farewell, Gul'sary!", "Chyngyz Aitmatov", BigDecimal.valueOf(2000)),
                                    new Book("The First Teacher", "Chyngyz Aitmatov", BigDecimal.valueOf(7000)),
                                    new Book("Short Stories: Dedicated to Writer's 85th Anniversary", "Chyngyz Aitmatov", BigDecimal.valueOf(4800)),
                                    new Book("Spotty Dog Running Along The Seashore", "Chyngyz Aitmatov", BigDecimal.valueOf(4890)),
                                    new Book("Issik-Kul", "Alykul Osmonov", BigDecimal.valueOf(4100)),
                                    new Book("Akkan suu", "Alykul Osmonov", BigDecimal.valueOf(4320)),
                                    new Book("Yrlar jyinagy", "Alykul Osmonov", BigDecimal.valueOf(7400))));
                }
                case "3"->bookService.getAllBooks().forEach(System.out::println);
                case "4"->{
                    System.out.println("Input book id you want to find: ");
                    try{
                        System.out.println(bookService.findById(new Scanner(System.in).nextLong()));
                    }catch (RuntimeException e){
                        System.err.println(e.getMessage());
                    }
                }
                case "5"->{
                    System.out.println("Write book id you want to delete: ");
                    System.out.println(bookService.deleteBookById(new Scanner(System.in).nextLong()));
                }
                case "6"->{
                    System.out.println("Write book id you want to update: ");
                    Long id=scanNum.nextLong();
                    System.out.println("Write new book's name: ");
                    String name=scanWord.nextLine();
                    System.out.println("Write new book's author: ");
                    String author=scanWord.nextLine();
                    System.out.println("Write new book's price: ");
                    int price=scanNum.nextInt();
                    System.out.println(bookService.updateBookById(id,new Book(name,author,BigDecimal.valueOf(price))));
                }
                case "7"->{
                    System.out.println("Input author name to get books : ");
                    System.out.println(bookService.getBookByAuthor(new Scanner(System.in).nextLine()));
                }
                case "8"->{
                    System.out.println("Input asc or desc :");
                    bookService.sortByPrice(new Scanner(System.in).nextLine()).forEach(System.out::println);
                }
                case "9"->{
                    System.out.println("Input author's name: ");
                    System.out.println(bookService.avgPriceOfAuthorBooks(new Scanner(System.in).nextLine()));
                }
                case "10"->System.out.println(bookService.cleanTableBook());


                }
        }
    }
}
