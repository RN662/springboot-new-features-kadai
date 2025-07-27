package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
public class ReviewController {
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final ReviewService reviewService;
	
	public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository, ReviewService reviewService) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.reviewService = reviewService;
	}
	
	/*
	@GetMapping("/reviews")
	public String index(Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		Page<Review> reviewPage = reviewRepository.findAll(pageable);
		
		model.addAttribute("reviewPage", reviewPage);
		
		return "reviews/index";
	}
	*/
	
	@GetMapping("/houses/{id}/reviews")
	public String showReviewList(@PathVariable(name = "id") Integer id, Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		House house = houseRepository.getReferenceById(id);
		Page<Review> reviewPage = reviewRepository.findByHouseId(id, pageable);
		
		User loginUser = null;
		if (userDetailsImpl != null) {
			loginUser = userDetailsImpl.getUser();
		}
		
		model.addAttribute("house", house);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("loginUser", loginUser);
		
		return "reviews/index";
	}
	
	@GetMapping("/houses/{id}/reviews/register")
	public String register(@PathVariable(name = "id") Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);
		
		model.addAttribute("house", house);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());
		
		return "reviews/register";
	}
	
	@PostMapping("/houses/{id}/reviews")
	public String create(@PathVariable(name = "id") Integer houseId, Model model, @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		if (bindingResult.hasErrors()) {
			House house = houseRepository.getReferenceById(houseId);
			model.addAttribute("house", house);
			
			return "reviews/register";
		}
		
		User user = userDetailsImpl.getUser();
		
		reviewService.create(houseId, reviewRegisterForm, user);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");
		
		return "redirect:/houses/" + houseId;
	}
	
	@GetMapping("/houses/{houseId}/reviews/{reviewId}/edit")
	public String edit(@PathVariable(name = "houseId") Integer houseId, @PathVariable(name = "reviewId") Integer reviewId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {		
		Review review = reviewRepository.findById(reviewId)
	      .orElseThrow(() -> new RuntimeException("レビューが見つかりません"));
		
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getReviewComment());
		
		model.addAttribute("reviewEditForm", reviewEditForm);
		model.addAttribute("house", review.getHouse());
		
		return "reviews/edit";
	}
	
	@PostMapping("/houses/{id}/reviews/update")
	public String update(@PathVariable(name = "id") Integer houseId, Model model, @ModelAttribute @Validated ReviewEditForm reviewEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			House house = houseRepository.getReferenceById(houseId);
			model.addAttribute("house", house);
			
			return "reviews/edit";
		}
		
		reviewService.update(reviewEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");
		
		return "redirect:/houses/" + houseId;
	}
	
	@GetMapping("/houses/{id}/reviews/show")
	public String show(@PathVariable(name = "id") Integer id, Model model,@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		House house = houseRepository.getReferenceById(id);
		Page<Review> reviewPage = reviewRepository.findByHouseId(id, pageable);
		User loginUser = userDetailsImpl.getUser();
		
		model.addAttribute("house", house);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("loginUser", loginUser);
		
		return "houses/show";
	}
	
	@PostMapping("/houses/{houseId}/reviews/{reviewId}/delete")
	public String delete(@PathVariable(name = "houseId") Integer houseId, @PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
		reviewRepository.deleteById(reviewId);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");
		
		return "redirect:/houses/" + houseId;
	}

}
