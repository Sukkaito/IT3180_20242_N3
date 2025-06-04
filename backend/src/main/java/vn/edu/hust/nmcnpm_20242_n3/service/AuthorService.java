package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return (List<Author>) authorRepository.findAll();
    }

    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    public Author findById(int id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + id));
    }

    public Author addAuthor(Author author) {
        if (authorRepository.existsByName(author.getName())) {
            throw new IllegalArgumentException("Author with name " + author.getName() + " already exists");
        }
        return authorRepository.save(author);
    }

    public Author updateById(int id, Author author) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        existingAuthor.setName(author.getName());
        return authorRepository.save(existingAuthor);
    }

    @Transactional
    public void deleteById(int id) {
        if (!authorRepository.existsById(id)) {
            throw new IllegalArgumentException("Author with ID " + id + " does not exist");
        }
        authorRepository.deleteById(id);
    }
}