package Version2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

class Author {
    private transient String name;

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Author{name='" + name + "'}";
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        name = (String) in.readObject();
    }
}


class Book {
    private transient  String title;
    private transient Author author;

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(author.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        author = new Author((String) in.readObject());
    }
}

class Reader {
    private transient String name;

    public Reader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(name);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        name = (String) in.readObject();
    }
}


class Rental implements Serializable {
    private transient Book book;
    private transient Reader reader;

    public Rental(Book book, Reader reader) {
        this.book = book;
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public Reader getReader() {
        return reader;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(book.getTitle());
        out.writeObject(book.getAuthor().getName());
        out.writeObject(reader.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String bookTitle = (String) in.readObject();
        String authorName = (String) in.readObject();
        String readerName = (String) in.readObject();
        this.book = new Book(bookTitle, new Author(authorName));
        this.reader = new Reader(readerName);
    }

    @Override
    public String toString() {
        return "Rental{book=" + book + ", reader=" + reader + "}";
    }
}


class Bookshelf implements Serializable {
    private transient List<Book> books;

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(books.size());
        for (Book b : books) {
            out.writeObject(b.getTitle());
            out.writeObject(b.getAuthor().getName());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        books = new ArrayList<>();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String title = (String) in.readObject();
            String authorName = (String) in.readObject();
            Book book = new Book(title, new Author(authorName));
            books.add(book);
        }
    }
}

class Library implements Serializable {
    private transient List<Book> books;
    private transient List<Reader> readers;
    private transient List<Author> authors;
    private List<Rental> rentals;
    private Bookshelf bookshelf;

    public Library() {
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.rentals = new ArrayList<>();
        this.bookshelf = new Bookshelf();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.books = new ArrayList<>(); // Ініціалізуємо список книг після десеріалізації
        this.readers = new ArrayList<>(); // Ініціалізуємо список читачів після десеріалізації
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
            String bookEntry = i < books.size() && books.get(i) != null ? "\"" + books.get(i).getTitle() + "\" - " + books.get(i).getAuthor().getName() : "";
            String readerEntry = i < readers.size() && readers.get(i) != null ? readers.get(i).getName() : "";
            String rentalEntry = i < rentals.size() && rentals.get(i) != null && rentals.get(i).getBook() != null ? "\"" + rentals.get(i).getBook().getTitle() + "\" - " + rentals.get(i).getReader().getName() : "";
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

public class Main2 {
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
        library.addBooksToBookshelf(booksToAddToShelf);

        System.out.println("\nCurrent state of the library:\n");
        System.out.println(library);

        try {
            library.serialize("library2.dat");
            System.out.println("\nLibrary serialized successfully.\n\n");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }

        try {
            Library deserializedLibrary = Library.deserialize("library2.dat");
            System.out.println("Deserialized library:");
            System.out.println(deserializedLibrary);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
    }
}