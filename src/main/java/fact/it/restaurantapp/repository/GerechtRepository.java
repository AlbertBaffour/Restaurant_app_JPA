package fact.it.restaurantapp.repository;

import fact.it.restaurantapp.model.Gerecht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GerechtRepository extends JpaRepository<Gerecht,Long> {
}
