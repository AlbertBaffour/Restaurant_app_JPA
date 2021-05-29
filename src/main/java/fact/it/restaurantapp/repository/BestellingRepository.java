package fact.it.restaurantapp.repository;

import fact.it.restaurantapp.model.Bestelling;
import fact.it.restaurantapp.model.Personeel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BestellingRepository extends JpaRepository<Bestelling,Long> {

}
