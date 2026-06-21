package com.example.propertymgmt.controller;

import com.example.propertymgmt.dto.PropertyDtos.OccupancyDashboard;
import com.example.propertymgmt.service.DashboardService;
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

    @GetMapping("/occupancy")
    public OccupancyDashboard getOccupancyStats() {
        return dashboardService.getOccupancyStats();
    }
}
