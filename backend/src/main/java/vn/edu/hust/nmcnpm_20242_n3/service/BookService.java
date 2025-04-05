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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        Publisher publisher = publisherService.getOrCreatePublisher(bookDTO.getPublisherName());

        // Get or create authors
        Set<Author> authors = bookDTO.getAuthorNames().stream()
                .map(authorService::getOrCreateAuthor)
                .collect(Collectors.toSet());

        // Get or create categories
        Set<Category> categories = bookDTO.getCategoryNames().stream()
                .map(categoryService::getOrCreateCategory)
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

    public List<Book> searchByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    public List<Book> searchByPublisherName(String publisherName) {
        return bookRepository.searchByPublisherName(publisherName);
    }

    public List<Book> searchByCategoryName(String categoryName) {
        return bookRepository.searchByCategoryName(categoryName);
    }

    public List<Book> searchByAuthorName(String authorName) {
        return bookRepository.searchByAuthorName(authorName);
    }

    @Transactional
    public void deleteByTitle(String title) {
        if (bookRepository.findByTitle(title).isEmpty()) {
            throw new IllegalArgumentException("Book with title " + title + " does not exist");
        }
        bookRepository.deleteByTitle(title);
    }

    public Book updateByTitle(String title, BookDTO bookDTO){
        Book book = findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        // Get or create publisher
        Publisher publisher = publisherService.getOrCreatePublisher(bookDTO.getPublisherName());

        Set<Author> authors = bookDTO.getAuthorNames().stream()
                .map(authorService::getOrCreateAuthor)
                .collect(Collectors.toSet());

        Set<Category> categories = bookDTO.getCategoryNames().stream()
                .map(categoryService::getOrCreateCategory)
                .collect(Collectors.toSet());

        book.setTitle(bookDTO.getTitle());
        book.setDescription(bookDTO.getDescription());
        book.setPublisher(publisher);
        book.setAuthors(authors);
        book.setCategories(categories);

        return bookRepository.save(book);
    }

    public BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPublisherName(book.getPublisher().getName());

        Set<String> authorNames = book.getAuthors().stream()
                .map(Author::getName)
                .collect(Collectors.toSet());
        dto.setAuthorNames(authorNames);

        Set<String> categoryNames = book.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
        dto.setCategoryNames(categoryNames);

        return dto;
    }
}