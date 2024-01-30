package edu.jcourse.http.controller;

import edu.jcourse.dto.review.ReviewCreateEditDto;
import edu.jcourse.dto.user.AdaptedUserDetails;
import edu.jcourse.service.ReviewService;
import edu.jcourse.validation.group.CreateAction;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static edu.jcourse.util.HttpPath.*;

@Controller
@RequestMapping(REVIEWS)
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(REVIEWS_BY_USER_ID)
    public String findAllByUserId(@PathVariable Long userId,
                                  RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("showReviews", true);
        redirectAttributes.addFlashAttribute("reviews", reviewService.findAllByUserId(userId));

        return "redirect:/users/{userId}";
    }

    @GetMapping(REVIEW_ADD)
    public String add(@PathVariable Integer movieId,
                      RedirectAttributes redirectAttributes,
                      @AuthenticationPrincipal Object userDetails,
                      @ModelAttribute("review") ReviewCreateEditDto review,
                      Model model) {

        if (userDetails instanceof AdaptedUserDetails user) {
            redirectAttributes.addFlashAttribute("userId", user.getId());
        } else if (userDetails instanceof OidcUser user) {
            redirectAttributes.addFlashAttribute("userId", user.getUserInfo().getClaims().get("userId"));
        }

        redirectAttributes.addFlashAttribute("errors", model.getAttribute("errors"));
        redirectAttributes.addFlashAttribute("showAddReview", true);
        redirectAttributes.addFlashAttribute("review", review);
        return "redirect:/movies/" + movieId;
    }

    @PostMapping
    public String create(@ModelAttribute @Validated({Default.class, CreateAction.class}) ReviewCreateEditDto review,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        Integer movieId = review.movieId();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("review", review);
            return "redirect:/reviews/add/" + movieId;
        }
        reviewService.create(review);
        return "redirect:/movies/" + movieId;
    }
}