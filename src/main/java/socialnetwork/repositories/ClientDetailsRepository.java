package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.oauth.ClientDetails;

/**
 * Created by Roman on 10.03.2018 14:43.
 */
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, String> {
}
