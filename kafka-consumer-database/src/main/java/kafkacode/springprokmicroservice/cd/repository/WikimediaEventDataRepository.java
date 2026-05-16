package kafkacode.springprokmicroservice.cd.repository;

import kafkacode.springprokmicroservice.cd.entity.Wikimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WikimediaEventDataRepository extends JpaRepository<Wikimedia, UUID> {
}
