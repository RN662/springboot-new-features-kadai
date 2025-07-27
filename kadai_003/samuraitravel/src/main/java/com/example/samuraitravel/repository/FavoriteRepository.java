package com.example.samuraitravel.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	Page<Favorite> findByUser(User user, Pageable pageable);
	
	boolean existsByUserAndHouseId(User user, Integer houseId);
	
	Optional<Favorite> findByUserAndHouseId(User user, Integer houseId);

}
