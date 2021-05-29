package fact.it.restaurantapp.repository;

import fact.it.restaurantapp.model.Tafel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TafelRepository extends JpaRepository<Tafel,Long> {
}
