package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	
	public ReviewService(ReviewRepository reviewRepository, HouseRepository houseRepository) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(Integer houseId, ReviewRegisterForm reviewRegisterForm, User user) {
		Review review = new Review();
		House house = houseRepository.getReferenceById(houseId);
		
		review.setHouse(house);
		review.setScore(reviewRegisterForm.getScore());
		review.setReviewComment(reviewRegisterForm.getReviewComment());
		review.setUser(user);
		
		reviewRepository.save(review);
	}
	
	@Transactional
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		
		review.setScore(reviewEditForm.getScore());
		review.setReviewComment(reviewEditForm.getReviewComment());
		
		reviewRepository.save(review);
	}

}
