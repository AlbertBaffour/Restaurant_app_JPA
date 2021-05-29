package fact.it.restaurantapp.repository;

import fact.it.restaurantapp.model.BesteldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BestelItemRepository extends JpaRepository<BesteldItem,Long> {
        List<BesteldItem> findAllByBestellingId(Long bestellingId);
}
