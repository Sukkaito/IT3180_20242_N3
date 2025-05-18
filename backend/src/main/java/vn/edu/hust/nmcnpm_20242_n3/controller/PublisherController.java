package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Publisher> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/search/{name}") // Get By Name
    public Publisher getPublisherByName(@PathVariable String name) {
        return publisherService.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Publisher with name " + name + " not found"));
    }

    @PostMapping // Add New
    public Publisher addPublisher(@RequestBody Publisher publisher) {
        return publisherService.addPublisher(publisher);
    }

    @PutMapping("/update/{id}") // Update By Id
    public Publisher updatePublisher(@PathVariable int id, @RequestBody Publisher publisher) {
        return publisherService.updateById(id, publisher);
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public void deletePublisher(@PathVariable int id) {
        publisherService.deleteById(id);
    }
}
