package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.entity.Category;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final CategoryService categoryService;

    @Autowired
    public BookService(
            BookRepository bookRepository,
            AuthorService authorService,
            PublisherService publisherService,
            CategoryService categoryService
    ) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.categoryService = categoryService;
    }

    @Transactional
    public Book addBook(BookDTO bookDTO) {
        if (bookRepository.findByTitle(bookDTO.getTitle()).isPresent()) {
            throw new IllegalArgumentException("Book with title " + bookDTO.getTitle() + " already exists");
        }

        // Get or create publisher
        Publisher publisher = publisherService.findById(bookDTO.getPublisherId());

        // Get or create authors
        Set<Author> authors = bookDTO.getAuthorIds().stream()
                .map(authorService::findById)  // Lấy Author bằng Integer ID
                .collect(Collectors.toSet());

        // Get or create categories
        Set<Category> categories = bookDTO.getCategoryIds().stream()
                .map(categoryService::findById)  // Lấy Category bằng Integer ID
                .collect(Collectors.toSet());

        // Create new book
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setCategories(categories);

        return bookRepository.save(book);
    }

    public Optional<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> findAllBooks() {
        return (List<Book>) bookRepository.findAll();
    }

    public Book searchById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    public List<Book> searchByPublisherId(int publisherId) {
        return bookRepository.searchByPublisherId(publisherId);
    }

    public List<Book> searchByCategoryId(int categoryId) {
        return bookRepository.searchByCategoryId(categoryId);
    }

    public List<Book> searchByAuthorId(int authorId) {
        return bookRepository.searchByAuthorId(authorId);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }
        bookRepository.deleteById(id);
    }

    public Book updateByTitle(int id, BookDTO bookDTO){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        // Get or create publisher
        Publisher publisher = publisherService.findById(bookDTO.getPublisherId());

        Set<Author> authors = bookDTO.getAuthorIds().stream()
                .map(authorService::findById)
                .collect(Collectors.toSet());

        Set<Category> categories = bookDTO.getCategoryIds().stream()
                .map(categoryService::findById)
                .collect(Collectors.toSet());

        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setCategories(categories);

        return bookRepository.save(book);
    }

    public BookDTO convertToDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getBookId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setDescription(book.getDescription());

        // Set publisher ID if publisher exists
        if (book.getPublisher() != null) {
            bookDTO.setPublisherId(book.getPublisher().getId());
        }

        // Convert Author entities to author IDs
        Set<Integer> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
        bookDTO.setAuthorIds(authorIds);

        // Convert Category entities to category IDs
        Set<Integer> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDTO.setCategoryIds(categoryIds);

        return bookDTO;
    }
}