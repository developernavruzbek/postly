package org.example.user


import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/user")
class UserController(
    private val userService: UserService,
){
    @GetMapping
    fun getAll() = userService.getAll()

    @PostMapping
    fun create(@RequestBody userCreateRequest: UserCreateRequest)  = userService.create(userCreateRequest)

    @GetMapping("/{userId}")
    fun getOne(@PathVariable userId: Long) = userService.getById(userId)

    @DeleteMapping("/{userId}")
    fun delete(@PathVariable userId: Long) = userService.delete(userId)

}

@RestController
@RequestMapping("api/v1/follows")
class FollowController(
    private val userFollowService: UserFollowService,
){

    @PostMapping("/follow")
    fun follow(@RequestBody userFollowCreateRequest: UserFollowCreateRequest) = userFollowService.follow(userFollowCreateRequest)

    @DeleteMapping("/unfollow")
    fun unfollow(@RequestBody userFollowCreateRequest: UserFollowCreateRequest) = userFollowService.unfollow(userFollowCreateRequest)

    @PutMapping("/confirm")
    fun confirmFollow(@RequestBody userFollowCreateRequest: UserFollowCreateRequest) = userFollowService.confirmFollow(userFollowCreateRequest)

    @GetMapping("/isFollowed/{followerId}/{followingId}")
    fun isFollowed(@PathVariable followerId: Long, @PathVariable followingId: Long) = userFollowService.existFollow(followerId, followingId)
}