package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.PublisherDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Book;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.repository.PublisherRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherDTO> getAllPublishers() {
        List<Publisher> publishers = (List<Publisher>) publisherRepository.findAll();
        
        return publishers.stream()
                .map(this::convertToDTO)
                .sorted((java.util.Comparator.comparing(PublisherDTO::getId)))
                .toList();
    }

    public Optional<PublisherDTO> findByName(String name) {
        return publisherRepository.findByName(name)
                .map(this::convertToDTO);
    }

    public Publisher findById(int id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with ID: " + id));
    }

    public PublisherDTO getPublisherById(int id) {
        return publisherRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with ID: " + id));
    }

    public PublisherDTO addPublisher(PublisherDTO dto) {
        if (publisherRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Publisher with name " + dto.getName() + " already exists");
        }
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        return convertToDTO(publisherRepository.save(publisher));
    }

    public PublisherDTO updateById(int id, PublisherDTO dto) {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        existingPublisher.setName(dto.getName());
        return convertToDTO(publisherRepository.save(existingPublisher));
    }

    @Transactional
    public void deleteById(int id) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher with ID " + id + " does not exist"));

        for (Book book : publisher.getBooks()) {
            book.setPublisher(null); // Unlink books from publisher
        }

        publisherRepository.deleteById(id);
    }
    
    private PublisherDTO convertToDTO(Publisher publisher) {
        return new PublisherDTO(publisher.getId(), publisher.getName());
    }
}
