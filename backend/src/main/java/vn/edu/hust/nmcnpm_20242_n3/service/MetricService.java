package vn.edu.hust.nmcnpm_20242_n3.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.repository.*;

@Service
@AllArgsConstructor
public class MetricService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final UserRepository userRepository;
    private final BookLoanRepository bookLoanRepository;
    private final BookRequestRepository bookRequestRepository;

    public long getBookCount() {
        return bookRepository.count();
    }


    public long getCategoryCount() {
        return categoryRepository.count();
    }


    public long getAuthorCount() {
         return authorRepository.count();
    }

    public long getPublisherCount() {
        return publisherRepository.count();
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public long getLoanCount() {
        return bookLoanRepository.count();
    }

    public long getRequestCount() {
        return bookRequestRepository.count();
    }
}
