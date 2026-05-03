package Hongik_SafeMap_Server.domain.admin.system.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class MaintenanceService {

    private final AtomicBoolean maintenanceMode = new AtomicBoolean(false);

    public boolean isUnderMaintenance() {
        return maintenanceMode.get();
    }

    public void setMaintenance(boolean status) {
        maintenanceMode.set(status);
    }
}
