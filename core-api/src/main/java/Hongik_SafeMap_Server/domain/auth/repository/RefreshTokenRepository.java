package Hongik_SafeMap_Server.domain.auth.repository;

import Hongik_SafeMap_Server.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    default Optional<RefreshToken> findByEmail(String email) {
        return findById(email);
    }

    default void deleteByEmail(String email) {
        deleteById(email);
    }
}
