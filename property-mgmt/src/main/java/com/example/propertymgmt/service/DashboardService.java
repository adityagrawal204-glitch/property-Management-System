package com.example.propertymgmt.service;

import com.example.propertymgmt.dto.PropertyDtos.OccupancyDashboard;
import com.example.propertymgmt.model.Property;
import com.example.propertymgmt.model.Unit;
import com.example.propertymgmt.model.UnitStatus;
import com.example.propertymgmt.model.User;
import com.example.propertymgmt.repository.PropertyRepository;
import com.example.propertymgmt.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardService {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final CurrentUserService currentUserService;

    public DashboardService(PropertyRepository propertyRepository, UnitRepository unitRepository,
                             CurrentUserService currentUserService) {
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.currentUserService = currentUserService;
    }

    /**
     * Computes occupancy stats across every Unit the current user owns
     * (across all their Properties). This is the core "real metric" feature
     * that goes beyond plain CRUD - it aggregates across a relationship
     * (Property -> Unit) rather than just listing rows from one table.
     */
    public OccupancyDashboard getOccupancyStats() {
        User owner = currentUserService.getCurrentUser();

        long totalUnits = unitRepository.countByProperty_Owner_Id(owner.getId());
        long occupiedUnits = unitRepository.countByProperty_Owner_IdAndStatus(owner.getId(), UnitStatus.OCCUPIED);

        // Sum rent across all units this owner has, split by occupied vs all,
        // to also surface "money currently coming in" vs "money possible if fully occupied".
        List<Property> properties = propertyRepository.findByOwner(owner);

        BigDecimal totalCollected = BigDecimal.ZERO;
        BigDecimal totalPotential = BigDecimal.ZERO;

        for (Property property : properties) {
            for (Unit unit : property.getUnits()) {
                if (unit.getRentAmount() == null) continue;
                totalPotential = totalPotential.add(unit.getRentAmount());
                if (unit.getStatus() == UnitStatus.OCCUPIED) {
                    totalCollected = totalCollected.add(unit.getRentAmount());
                }
            }
        }

        return new OccupancyDashboard(totalUnits, occupiedUnits, totalCollected, totalPotential);
    }
}
