package net.tislib.restaurantapp.controller;

import lombok.RequiredArgsConstructor;
import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.data.PageContainer;
import net.tislib.restaurantapp.data.ReviewResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static net.tislib.restaurantapp.constant.ApiConstants.API_REVIEWS;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_ID;
import static net.tislib.restaurantapp.constant.ApiConstants.PATH_OWNER_REPLY;

@RestController
@RequestMapping(API_REVIEWS)
@RequiredArgsConstructor
public class ReviewController {

    @PostMapping
    public ReviewResource create(@PathVariable Long restaurantId,
                                 @RequestBody @Validated ReviewResource userResource) {
        return null;
    }

    @GetMapping
    public PageContainer<ReviewResource> list(@PathVariable Long restaurantId) {
        return null;
    }

    @GetMapping(PATH_ID)
    public ReviewResource get(@PathVariable Long restaurantId,
                              @PathVariable Long id) {
        return null;
    }

    @PutMapping(PATH_ID)
    public ReviewResource update(@PathVariable Long restaurantId,
                                 @PathVariable Long id,
                                 @RequestBody @Validated ReviewResource userResource) {
        return null;
    }

    @PutMapping(PATH_OWNER_REPLY)
    public ReviewResource ownerReply(@PathVariable Long restaurantId,
                                     @PathVariable Long id,
                                     @RequestBody @Validated OwnerReplyResource ownerReplyResource) {
        return null;
    }

}
