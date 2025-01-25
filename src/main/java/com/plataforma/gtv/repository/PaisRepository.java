package com.plataforma.gtv.repository;

import com.plataforma.gtv.domain.Pais;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pais entity.
 */
@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {
    default Optional<Pais> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pais> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pais> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select pais from Pais pais left join fetch pais.local", countQuery = "select count(pais) from Pais pais")
    Page<Pais> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pais from Pais pais left join fetch pais.local")
    List<Pais> findAllWithToOneRelationships();

    @Query("select pais from Pais pais left join fetch pais.local where pais.id =:id")
    Optional<Pais> findOneWithToOneRelationships(@Param("id") Long id);
}
