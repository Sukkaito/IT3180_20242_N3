package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<Publisher> getAllPublishers() {
        return (List<Publisher>) publisherRepository.findAll();
    }

    public Optional<Publisher> findByName(String name) {
        return publisherRepository.findByName(name);
    }

    public Publisher findById(int id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found with ID: " + id));
    }

    public Publisher addPublisher(Publisher publisher) {
        if (publisherRepository.existsByName(publisher.getName())) {
            throw new IllegalArgumentException("Publisher with name " + publisher.getName() + " already exists");
        }
        return publisherRepository.save(publisher);
    }

    public Publisher updateById(int id, Publisher publisher) {
        Publisher existingPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        existingPublisher.setName(publisher.getName());
        return publisherRepository.save(existingPublisher);
    }

    @Transactional
    public void deleteById(int id) {
        if (!publisherRepository.existsById(id)) {
            throw new IllegalArgumentException("Publisher with ID " + id + " does not exist");
        }
        publisherRepository.deleteById(id);
    }
}
