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

    public List<BookCopy> getAllAvailableBookCopies() {
        return bookCopyRepository.findByStatus(BookCopyStatusEnum.valueOf(BookCopyStatusEnum.AVAILABLE.name()));
    }


    public Object getBookCopiesByBookId(Integer bookId) {
        List<BookCopy> bookCopies = bookCopyRepository.findByOriginalBook_BookId(bookId);
        if (bookCopies.isEmpty()) {
            return "No available book copies found for the given book ID.";
        }
        return bookCopies;
    }
}
