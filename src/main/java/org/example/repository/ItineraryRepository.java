package org.example.repository;

import org.example.model.entity.Itinerary;
//import org.example.model.entity.User;
import org.example.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {

    List<Itinerary> findByUserOrderByCreatedAtDesc(Users user);

    @Query("SELECT i FROM Itinerary i WHERE i.user = :user AND (i.destination LIKE %:searchTerm% OR i.fullItinerary LIKE %:searchTerm%) ORDER BY i.createdAt DESC")
    List<Itinerary> findByUserAndSearchTerm(@Param("user") Users user, @Param("searchTerm") String searchTerm);

}