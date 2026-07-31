package com.zuno.repository;

import com.zuno.model.Listing;
import com.zuno.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ListingRepository extends JpaRepository<Listing, UUID> {

    Page<Listing> findByIsActiveTrue(Pageable pageable);

    List<Listing> findByOwnerAndIsActiveTrue(User owner);

    @Query("SELECT l FROM Listing l WHERE l.isActive = true " +
           "AND (:area IS NULL OR LOWER(l.area) LIKE LOWER(CONCAT('%', :area, '%'))) " +
           "AND (:maxBudget IS NULL OR l.monthlyRent <= :maxBudget) " +
           "AND (:gender IS NULL OR l.genderPreference = :gender OR l.genderPreference = 'ANY')")
    Page<Listing> searchListings(
            @Param("area") String area,
            @Param("maxBudget") Integer maxBudget,
            @Param("gender") Listing.GenderPreference gender,
            Pageable pageable);

    long countByIsActiveTrue();

    @Query("SELECT SUM(l.vacantBeds) FROM Listing l WHERE l.isActive = true")
    Long countTotalVacantBeds();
}
