package Hongik_SafeMap_Server.domain.terms.repository;

import Hongik_SafeMap_Server.domain.terms.domain.Terms;
import Hongik_SafeMap_Server.domain.terms.domain.TermsVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    @Query("SELECT t FROM Terms t JOIN FETCH t.termsVersion")
    List<Terms> findAllWithVersion();

    @Query("SELECT t FROM Terms t JOIN FETCH t.termsVersion WHERE t.id IN :ids")
    List<Terms> findAllByIdWithVersion(@Param("ids") List<Long> ids);

    @Query("SELECT t FROM Terms t JOIN FETCH t.termsVersion tv WHERE tv = :termsVersion")
    List<Terms> findAllByTermsVersion(@Param("termsVersion") TermsVersion termsVersion);
}
