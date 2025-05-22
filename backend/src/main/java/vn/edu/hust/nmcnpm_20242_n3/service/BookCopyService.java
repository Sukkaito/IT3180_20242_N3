package vn.edu.hust.nmcnpm_20242_n3.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy;
import vn.edu.hust.nmcnpm_20242_n3.repository.BookCopyRepository;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

import java.util.List;


@RequiredArgsConstructor
@Service
public class BookCopyService {

    @Autowired
    private BookCopyRepository bookCopyRepository;

    public boolean isBookCopyExists(int  bookCopyId) {
        return bookCopyRepository.existsById(bookCopyId);
    }

    public List<BookCopy> getAllAvailableBookCopies() {
        return bookCopyRepository.findByStatus(BookCopyStatusEnum.valueOf(BookCopyStatusEnum.AVAILABLE.name()));
    }

    public BookCopy getBookCopyById(int bookCopyId) {
        return bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found with id: " + bookCopyId));
    }


    public void setStatus(int bookCopyId, BookCopyStatusEnum status) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found with id: " + bookCopyId));
        bookCopy.setStatus(status);
        bookCopyRepository.save(bookCopy);
    }

    public boolean isAvailable (int bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new IllegalArgumentException("Book copy not found with id: " + bookCopyId));
        return  bookCopy.getStatus().equals(BookCopyStatusEnum.AVAILABLE);
    }


}
