package vn.edu.hust.nmcnpm_20242_n3.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hust.nmcnpm_20242_n3.dto.PublisherDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.repository.PublisherRepository;

import java.util.Optional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Publisher addPublisher(PublisherDTO publisherDTO) {
        if (publisherRepository.existsByName(publisherDTO.getName())) {
            throw new IllegalArgumentException("Publisher with name " + publisherDTO.getName() + " already exists");
        }

        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        return publisherRepository.save(publisher);
    }

    public Optional<Publisher> findByName(String name) {
        return publisherRepository.findByName(name);
    }

    @Transactional
    public void deleteByName(String name) {
        if (!publisherRepository.existsByName(name)) {
            throw new IllegalArgumentException("Publisher with name " + name + " does not exist");
        }
        publisherRepository.deleteByName(name);
    }
    public Publisher updateByName(String name, PublisherDTO publisherDTO) {
        Publisher publisher = findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));
        publisher.setName(publisherDTO.getName());
        return publisherRepository.save(publisher);
    }

    public Publisher getOrCreatePublisher(String name) {
        return publisherRepository.findByName(name)
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setName(name);
                    return publisherRepository.save(newPublisher);
                });
    }
}