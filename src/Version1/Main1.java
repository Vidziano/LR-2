package Version1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


class Author implements Serializable {
    private String name;

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{name='" + name + "'}";
    }
}

class Book implements Serializable {
    private String title;
    private Author author;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author=" + author + "}";
    }
}

class Reader implements Serializable {
    private String name;

    public Reader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }
}

class Rental implements Serializable {
    private Book book;
    private Reader reader;

    public Rental(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public String toString() {
        return "Rental{book=" + book + ", reader=" + reader + "}";
    }
}

class Bookshelf implements Serializable {
    private List<Book> books;

    public Bookshelf() {
        this.books = new ArrayList<>();
    }

    public void addBooks(List<Book> booksToAdd) {
        books.addAll(booksToAdd);
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bookshelf{");
        for (Book book : books) {
            sb.append(book).append("\\n");
        }
        sb.append('}');
        return sb.toString();
    }
}

class Library implements Serializable {
    private List<Book> books;
    private List<Reader> readers;
    private List<Author> authors;
    private List<Rental> rentals;
    private Bookshelf bookshelf;

    public Library() {
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.rentals = new ArrayList<>();
        this.bookshelf = new Bookshelf();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addReader(Reader reader) {
        readers.add(reader);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public void addBooksToBookshelf(List<Book> books) {
        bookshelf.addBooks(books);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("", Collections.nCopies(142, "-"))).append("\n");
        String headerFormat = "| %-55s | %-20s | %-30s | %-25s |%n";
        String rowFormat = "| %-55.40s | %-20.25s | %-30.40s | %-25.25s |%n";

        sb.append(String.format(headerFormat, "Books (Title - Author)", "Readers", "Rentals", "Bookshelf"));
        sb.append(String.join("", Collections.nCopies(142, "-"))).append("\n");

        int maxRows = Math.max(books.size(), Math.max(readers.size(), rentals.size()));

        for (int i = 0; i < maxRows; i++) {
            String bookEntry = i < books.size() ? "\"" + books.get(i).getTitle() + "\" - " + books.get(i).getAuthor().getName() : "";
            String readerEntry = i < readers.size() ? readers.get(i).getName() : "";
            String rentalEntry = i < rentals.size() ? "\"" + rentals.get(i).getBook().getTitle() + "\" - " + rentals.get(i).getReader().getName() : "";
            String bookshelfEntry = (i < bookshelf.getBooks().size()) ? "\"" + bookshelf.getBooks().get(i).getTitle() + "\"" : "";

            sb.append(String.format(rowFormat, bookEntry, readerEntry, rentalEntry, bookshelfEntry));
        }

        sb.append(String.join("", Collections.nCopies(142, "-"))).append("\n");
        return sb.toString();
    }




    public void serialize(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    public static Library deserialize(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Library) in.readObject();
        }
    }
}


public class Main1 {
    public static void main(String[] args) {
        Library library = new Library();

        Author author1 = new Author("Elizabeth Strout");
        Author author2 = new Author("Antoine de Exupery");

        Book book1 = new Book("Anything is Possible", author1);
        Book book2 = new Book("The Little Prince", author2);

        library.addBook(book1);
        library.addBook(book2);

        Reader reader1 = new Reader("Alex");
        Reader reader2 = new Reader("Bill");

        library.addReader(reader1);
        library.addReader(reader2);

        library.addAuthor(author1);
        library.addAuthor(author2);

        Rental rental1 = new Rental(book2, reader2);

        library.addRental(rental1);

        List<Book> booksToAddToShelf = new ArrayList<>();
        booksToAddToShelf.add(book1);
        booksToAddToShelf.add(book2);
        library.addBooksToBookshelf(booksToAddToShelf);

        System.out.println("\nCurrent state of the library:\n");
        System.out.println(library);

        try {
            library.serialize("library1.dat");
            System.out.println("\nLibrary serialized successfully.\n\n");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }

        try {
            Library deserializedLibrary = Library.deserialize("library1.dat");
            System.out.println("Deserialized library:");
            System.out.println(deserializedLibrary);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
    }
}
