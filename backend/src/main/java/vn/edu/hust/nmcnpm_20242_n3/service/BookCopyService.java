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

    public boolean isBookCopyAvailable(String bookId) {
        return bookCopyRepository.existsByBookCopyIdAndStatus(bookId, BookCopyStatusEnum.AVAILABLE.name());
    }

    public boolean isBookCopyExists(String bookCopyId) {
        return bookCopyRepository.existsById(bookCopyId);
    }

    public List<BookCopy> getAllAvailableBookCopies() {
        return bookCopyRepository.findByStatus(BookCopyStatusEnum.AVAILABLE.name());
    }

    public Object getBookCopyById(String bookCopyId) {
        return bookCopyRepository.findById(bookCopyId);

    }


}
