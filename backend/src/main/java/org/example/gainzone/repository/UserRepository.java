package org.example.gainzone.repository;

import org.example.gainzone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM Activity a JOIN a.participants u WHERE a.id = :activityId")
    List<User> findAllByActivitiesId(@Param("activityId") Long activityId);
}
