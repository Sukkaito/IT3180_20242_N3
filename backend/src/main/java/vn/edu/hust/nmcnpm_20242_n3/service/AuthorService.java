package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.AuthorDTO;
import vn.edu.hust.nmcnpm_20242_n3.dto.PublisherDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.repository.AuthorRepository;

import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(AuthorDTO authorDTO) {
        if (authorRepository.existsByName(authorDTO.getName())) {
            throw new IllegalArgumentException("Author with name " + authorDTO.getName() + " already exists");
        }

        Author author = new Author();
        author.setName(authorDTO.getName());
        return authorRepository.save(author);
    }

    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Transactional
    public void deleteByName(String name) {
        if (!authorRepository.existsByName(name)) {
            throw new IllegalArgumentException("Author with name " + name + " does not exist");
        }
        authorRepository.deleteByName(name);
    }

    public Author updateByName(String name, AuthorDTO authorDTO) {
        Author author = findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        author.setName(authorDTO.getName());
        return authorRepository.save(author);
    }

    public Author getOrCreateAuthor(String name) {
        return authorRepository.findByName(name)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(name);
                    return authorRepository.save(newAuthor);
                });
    }
}