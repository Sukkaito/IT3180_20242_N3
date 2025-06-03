package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.AuthorDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Author;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
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

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = (List<Author>) authorRepository.findAll();

        return authors.stream()
                .map(this::convertToDTO)
                .sorted((java.util.Comparator.comparing(AuthorDTO::getId)))
                .toList();
    }

    public Optional<AuthorDTO> findByName(String name) {
        return authorRepository.findByName(name)
                .map(this::convertToDTO);
    }

    public AuthorDTO findById(int id) {
        return authorRepository.findById(id).map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + id));
    }

    public AuthorDTO addAuthor(AuthorDTO dto) {
        if (authorRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Author with name " + dto.getName() + " already exists");
        }
        Author author = new Author();
        author.setName(dto.getName());
        return convertToDTO(authorRepository.save(author));
    }

    public AuthorDTO updateById(int id, AuthorDTO dto) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        existingAuthor.setName(dto.getName());
        return convertToDTO(authorRepository.save(existingAuthor));
    }

    @Transactional
    public void deleteById(int id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + id + " does not exist"));

        for (Book book : author.getBooks()) {
            book.getAuthors().remove(author);
        }

        authorRepository.deleteById(id);
    }

    private AuthorDTO convertToDTO(Author author) {
        return new AuthorDTO(author.getId(), author.getName());
    }
}