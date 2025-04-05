package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.PublisherDTO;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.service.PublisherService;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<?> addPublisher(@RequestBody PublisherDTO publisherDTO) {
        try {
            Publisher publisher = publisherService.addPublisher(publisherDTO);
            return new ResponseEntity<>(publisher, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Publisher> findPublisherByName(@RequestParam String name) {
        return publisherService.findByName(name)
                .map(publisher -> new ResponseEntity<>(publisher, HttpStatus.OK))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePublisherByName(@RequestParam String name, @RequestBody PublisherDTO publisherDTO) {
        try {
            Publisher updatedPublisher = publisherService.updateByName(name, publisherDTO);
            return new ResponseEntity<>(updatedPublisher, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deletePublisherByName(@RequestParam String name) {
        try {
            publisherService.deleteByName(name);
            return new ResponseEntity<>("Publisher deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}