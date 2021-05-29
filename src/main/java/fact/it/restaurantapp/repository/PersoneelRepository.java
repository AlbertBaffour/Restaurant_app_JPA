package fact.it.restaurantapp.repository;

import fact.it.restaurantapp.model.Personeel;
import fact.it.restaurantapp.model.Zaalpersoneel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersoneelRepository extends JpaRepository<Personeel,Long> {

    @Query("select p from Zaalpersoneel p")
    List<Personeel> findAllZaalPersoneels();


}
