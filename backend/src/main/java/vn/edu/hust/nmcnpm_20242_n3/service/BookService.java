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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
                .map(authorService::findById)
                .collect(Collectors.toSet());

        // Get or create categories
        Set<Category> categories = bookDTO.getCategoryIds().stream()
                .map(categoryService::findById)
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

    public Book searchById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));
    }

    public List<Book> findAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }


    public List<Book> searchByTitle(String title) {
        return bookRepository.searchByTitle(title);
    }

    public List<Book> searchByPublisherId(Integer publisherId) {
        return bookRepository.searchByPublisherId(publisherId);
    }

    public List<Book> searchByCategoryId(Integer categoryId) {
        return bookRepository.searchByCategoryId(categoryId);
    }

    public List<Book> searchByAuthorId(Integer authorId) {
        return bookRepository.searchByAuthorId(authorId);
    }

    public List<Book> findBooksByPage(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("Page number must be 1 or greater.");
        }
        Page<Book> bookPage = bookRepository.findAll(PageRequest.of(page - 1, size)); // trừ 1 ở đây
        return bookPage.getContent();
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " does not exist");
        }
        bookRepository.deleteById(id);
    }

    public Book updateByTitle(Integer id, BookDTO bookDTO){
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
        BookDTO dto = new BookDTO();
        dto.setId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setPublisherId(book.getPublisher().getId());

        Set<Integer> authorIds = book.getAuthors().stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
        dto.setAuthorIds(authorIds);

        Set<Integer> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        dto.setCategoryIds(categoryIds);

        return dto;
    }
}