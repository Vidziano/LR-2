package Version3;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Author implements Externalizable {
    private String name;

    public Author() {
    }

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
    }

    @Override
    public String toString() {
        return "Author{name='" + name + "'}";
    }
}

class Book implements Externalizable {
    private String title;
    private Author author;

    public Book() {
    }

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(title);
        out.writeObject(author);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = in.readUTF();
        author = (Author) in.readObject();
    }

    @Override
    public String toString() {
        return "Book{title='" + title + "', author=" + author + "}";
    }
}

class Reader implements Externalizable {
    private String name;

    public Reader() {
    }

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
    }

    @Override
    public String toString() {
        return "Reader{name='" + name + "'}";
    }
}

class Rental implements Externalizable {
    private Book book;
    private Reader reader;

    public Rental() {
    }

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(book);
        out.writeObject(reader);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        book = (Book) in.readObject();
        reader = (Reader) in.readObject();
    }

    @Override
    public String toString() {
        return "Rental{book=\"" + book.getTitle() + "\", reader=" + reader.getName() + "}";
    }
}

class Bookshelf implements Externalizable {
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(books.size());
        for (Book book : books) {
            out.writeObject(book);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        books = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            books.add((Book) in.readObject());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bookshelf{");
        for (Book book : books) {
            sb.append("\"").append(book.getTitle()).append("\"").append("\\n");
        }
        sb.append('}');
        return sb.toString();
    }
}

class Library implements Externalizable {
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(books.size());
        for (Book book : books) {
            out.writeObject(book);
        }

        out.writeInt(readers.size());
        for (Reader reader : readers) {
            out.writeObject(reader);
        }

        out.writeInt(authors.size());
        for (Author author : authors) {
            out.writeObject(author);
        }

        out.writeInt(rentals.size());
        for (Rental rental : rentals) {
            out.writeObject(rental);
        }

        out.writeObject(bookshelf);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        books = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            books.add((Book) in.readObject());
        }

        size = in.readInt();
        readers = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            readers.add((Reader) in.readObject());
        }

        size = in.readInt();
        authors = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            authors.add((Author) in.readObject());
        }

        size = in.readInt();
        rentals = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rentals.add((Rental) in.readObject());
        }

        bookshelf = (Bookshelf) in.readObject();
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

public class Main3 {
    public static void main(String[] args) {
        Library library = new Library();

        Author author1 = new Author("Agatha Christie");
        Author author2 = new Author("J. K. Rowling");

        Book book1 = new Book("A Haunting in Venice", author1);
        Book book2 = new Book("Harry Potter", author2);

        library.addBook(book1);
        library.addBook(book2);

        Reader reader1 = new Reader("Thomas");
        Reader reader2 = new Reader("Kate");

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
            library.serialize("library3.dat");
            System.out.println("\nLibrary serialized successfully.\n\n");
        } catch (IOException e) {
            System.err.println("Error occurred during serialization: " + e.getMessage());
        }

        try {
            Library deserializedLibrary = Library.deserialize("library3.dat");
            System.out.println("Deserialized library:");
            System.out.println(deserializedLibrary);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred during deserialization: " + e.getMessage());
        }
    }
}
