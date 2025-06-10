package clb.backend.controllers;

import clb.backend.DTO.DashboardDTO;
import clb.backend.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        DashboardDTO dashboard = dashboardService.getUserDashboard(email);
        return ResponseEntity.ok(dashboard);
    }
}

