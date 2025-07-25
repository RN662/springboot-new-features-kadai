package com.example.samuraitravel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	Page<Review> findByHouseId(Integer houseId, Pageable pageable);
	Optional<Review> findByHouseAndUser(House house, User user);
	List<Review> findTop6ByHouseIdOrderByIdDesc(Integer houseId);

}
