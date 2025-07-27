package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.UserRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

@Controller

public class FavoriteController {
	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final FavoriteService favoriteService;
	
	public FavoriteController(FavoriteRepository favoriteRepository, UserRepository userRepository, FavoriteService favoriteService) {
		this.favoriteRepository = favoriteRepository;
		this.userRepository = userRepository;
		this.favoriteService = favoriteService;
	}
	
	@GetMapping("/favorites")
	public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		User loginUser = userDetailsImpl.getUser();
		
		Page<Favorite> favoritePage = favoriteRepository.findByUser(loginUser, pageable);
		
		model.addAttribute("favoritePage", favoritePage);
		
		return "favorites/index";
	}
	
	@PostMapping("/favorites/{houseId}")
	public String create(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		
		User loginUser = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		favoriteService.create(loginUser, houseId);
		
		return "redirect:/houses/" + houseId;
	}
	
	@PostMapping("/favorites/delete/{houseId}")
	public String delete(@PathVariable Integer houseId, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		
		User loginUser = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		favoriteService.delete(loginUser, houseId);
		
		return "redirect:/houses/" + houseId;
	}

}
