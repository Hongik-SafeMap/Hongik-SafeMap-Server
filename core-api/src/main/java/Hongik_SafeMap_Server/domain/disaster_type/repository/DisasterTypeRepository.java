package Hongik_SafeMap_Server.domain.disaster_type.repository;

import Hongik_SafeMap_Server.domain.disaster_type.domain.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisasterTypeRepository extends JpaRepository<DisasterType, Long> {

    boolean existsByName(String name);

    Optional<DisasterType> findByName(String name);

    List<DisasterType> findAllByOrderByIdAsc();
}
