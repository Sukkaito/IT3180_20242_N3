package vn.edu.hust.nmcnpm_20242_n3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;
import vn.edu.hust.nmcnpm_20242_n3.dto.BookBrowsingDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookBrowsingService {

    private final BookService bookService;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookBrowsingService(BookService bookService, BookCopyRepository bookCopyRepository) {
        this.bookService = bookService;
        this.bookCopyRepository = bookCopyRepository;
    }

    public List<BookBrowsingDTO> getAllBooksWithAvailableCopies() {
        List<Book> books = bookService.findAllBooks();

        return books.stream().map(book -> {
            int availableCount = bookCopyRepository
                .findByOriginalBook_BookIdAndStatus(book.getBookId(), BookCopyStatusEnum.AVAILABLE)
                .size();

            BookBrowsingDTO dto = new BookBrowsingDTO();
            dto.setId(book.getBookId());
            dto.setTitle(book.getTitle());
            dto.setDescription(book.getDescription());
            dto.setPublisherId(book.getPublisher().getId());
            dto.setAuthorIds(book.getAuthors().stream().map(a -> a.getId()).collect(Collectors.toSet()));
            dto.setCategoryIds(book.getCategories().stream().map(c -> c.getId()).collect(Collectors.toSet()));
            dto.setAvailableCopies(availableCount);
            return dto;
        }).collect(Collectors.toList());
    }
}
