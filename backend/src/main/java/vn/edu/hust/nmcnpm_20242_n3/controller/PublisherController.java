package vn.edu.hust.nmcnpm_20242_n3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hust.nmcnpm_20242_n3.dto.PublisherDTO;
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
    public List<PublisherDTO> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @GetMapping("/search/{name}") // Get By Name
    public PublisherDTO getPublisherByName(@PathVariable String name) {
        return publisherService.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Publisher with name " + name + " not found"));
    }

    @PostMapping // Add New
    public PublisherDTO addPublisher(@RequestBody PublisherDTO dto) {
        return publisherService.addPublisher(dto);
    }

    @PutMapping("/update/{id}") // Update By Id
    public PublisherDTO updatePublisher(@PathVariable int id, @RequestBody PublisherDTO dto) {
        return publisherService.updateById(id, dto);
    }

    @DeleteMapping("/delete/{id}") // Delete By Id
    public void deletePublisher(@PathVariable int id) {
        publisherService.deleteById(id);
    }


}
