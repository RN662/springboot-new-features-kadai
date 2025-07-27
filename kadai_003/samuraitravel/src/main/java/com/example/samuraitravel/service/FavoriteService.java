package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;

import jakarta.transaction.Transactional;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository, HouseRepository houseRepository) {
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
	}
	
	@Transactional
	public void create(User user, Integer houseId) {
		if (favoriteRepository.existsByUserAndHouseId(user, houseId)) {
			return;
		}
		
		House house = houseRepository.getReferenceById(houseId);
		
		Favorite favorite = new Favorite();
		favorite.setUser(user);
		favorite.setHouse(house);
		
		favoriteRepository.save(favorite);
	}
	
	@Transactional
	public void delete(User user, Integer houseId) {
		Favorite favorite = favoriteRepository.findByUserAndHouseId(user, houseId)
				.orElseThrow(() -> new IllegalArgumentException("お気に入りが見つかりません"));
		favoriteRepository.delete(favorite);
	}
}
