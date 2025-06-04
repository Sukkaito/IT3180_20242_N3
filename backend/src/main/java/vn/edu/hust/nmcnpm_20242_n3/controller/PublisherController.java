package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.entity.Publisher;
import vn.edu.hust.nmcnpm_20242_n3.service.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping // Get All
    public ResponseEntity<?> getAllPublishers() {
        try {
            List<Publisher> publishers = publisherService.getAllPublishers();
            return ResponseEntity.ok(publishers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving publishers: " + e.getMessage());
        }
    }

    @GetMapping("/search/{name}") // Get By Name
    public ResponseEntity<?> getPublisherByName(@PathVariable String name) {
        try {
            Publisher publisher = publisherService.findByName(name)
                    .orElseThrow(() -> new IllegalArgumentException("Publisher with name " + name + " not found"));
            return ResponseEntity.ok(publisher);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving publisher: " + e.getMessage());
        }
    }

    @PostMapping // Add New
    public ResponseEntity<?> addPublisher(@RequestBody Publisher publisher) {
        try {
            Publisher created = publisherService.addPublisher(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding publisher: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}") // Update By Id
    public ResponseEntity<?> updatePublisher(@PathVariable int id, @RequestBody Publisher publisher) {
        try {
            Publisher updated = publisherService.updateById(id, publisher);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating publisher: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public ResponseEntity<?> deletePublisher(@PathVariable int id) {
        try {
            publisherService.deleteById(id);
            return ResponseEntity.ok("Publisher deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting publisher: " + e.getMessage());
        }
    }
}
