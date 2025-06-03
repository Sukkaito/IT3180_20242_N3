package vn.edu.hust.nmcnpm_20242_n3.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.nmcnpm_20242_n3.service.ApplicationService;

@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/restart")
    public void restart() {
        applicationService.restart();
    }

    @PostMapping("/shutdown")
    public void shutdown() {
        applicationService.shutdown();
    }
}
