package org.example.user


import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService{

    fun create( userCreateRequest: UserCreateRequest)
    fun getById(id: Long): UserResponse
    fun update(userId:Long ,userUpdateRequest: UserUpdateRequest)
    fun delete(id: Long)
    fun getAll() : List<UserResponse>
    fun getProfileUserId(userId: Long) : MyProfile
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
    private val followingRepository: UserFollowingRepository,
    private val postClient: PostClient
) : UserService {

    @Transactional
    override fun create(userCreateRequest: UserCreateRequest) {
        userCreateRequest.run {
            userRepository.findByUsernameAndDeletedFalse(username)?.let {
                throw UserAlreadyExistsException()
            }
            userRepository.findByPhoneNumberAndDeletedFalse(phoneNumber)?.let {
                throw UserAlreadyExistsException()
            }
        }
        userRepository.save( userMapper.toEntity(userCreateRequest))
    }

    override fun getById(id: Long): UserResponse {
        userRepository.findByIdAndDeletedFalse(id)?.let { user->
            return userMapper.toDto(user)
        } ?: throw UserNotFoundException()
    }


    @Transactional
    override fun update(userId: Long, userUpdateRequest: UserUpdateRequest) {

        var user = userRepository.findByIdAndDeletedFalse(userId)
        if (user==null){
           throw UserNotFoundException()
        }
        userUpdateRequest.run {
            fullName?.let {
                if(!it.trim().isEmpty())
                    user.fullName = it
            }

            phoneNumber?.let {
                if (!it.trim().isEmpty()){
                    userRepository.findByPhoneNumberAndDeletedFalse(it)?.let {
                        throw PhoneNumberAlreadyExistsException()
                    }
                    user.phoneNumber = it
                }
            }

            username?.let {
                if (!it.trim().isEmpty()){
                    userRepository.findByUsernameAndDeletedFalse(it)?.let {
                        throw UserNameAlreadyExistsException()
                    }
                    user.phoneNumber  = it
                }
            }

            password?.let {
                if (!it.trim().isEmpty())
                    user.password = it
            }
            bio?.let {
                if (!it.trim().isEmpty())
                    user.bio = it
            }

            avatarUrl?.let {
                if (!it.trim().isEmpty())
                    user.avatarUrl = it
            }
            accountType?.let {
                user.accountType  = it
            }
            userRepository.save(user)

        }

    }

    override fun delete(id: Long) {
        userRepository.trash(id)
            ?: throw UserNotFoundException()
    }

    override fun getAll(): List<UserResponse> {
        return userRepository.findAllNotDeleted()
            .map { userMapper.toDto(it) }
    }

    override fun getProfileUserId(userId: Long): MyProfile {
        val user = userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()

        val myFollowers  = followingRepository.countByFollowingAndStatusAndDeletedFalse(user, FollowStatus.ACCEPTED)
        val myFollowings  = followingRepository.countByFollowerAndStatusAndDeletedFalse(user, FollowStatus.ACCEPTED)
        var postCount = postClient.countPostsByUserId(userId)

        return MyProfile(
            id = user.id,
            username = user.username,
            fullName = user.fullName,
            phoneNumber = user.phoneNumber,
            bio = user.bio,
            myFollowers = myFollowers,
            myFollowing =  myFollowings,
            myPostCount = postCount
        )
    }
}


interface UserFollowService{
    fun follow(userFollowCreateRequest: UserFollowRequest)
    fun unfollow(userFollowCreateRequest: UserFollowRequest)
    fun confirmFollow(userFollowConfirmRequest: UserFollowConfirmRequest)
    fun existFollow(followerId: Long, followingId:Long): Boolean
    fun followerByUserId(userId:Long) :List<UserResponse>
    fun followingByUserId(userId:Long) :List<UserResponse>
}


@Service
class UserFollowServiceImpl(
    private val followMapper: UserFollowMapper,
    private val userRepository: UserRepository,
    private val followingRepository: UserFollowingRepository,
    private val userMapper: UserMapper
) : UserFollowService {

    @Transactional
    override fun follow(userFollowCreateRequest: UserFollowRequest) {
        userFollowCreateRequest.run {
            var follower = userRepository.findByIdAndDeletedFalse(followerId)
                ?: throw UserNotFoundException()

            var following = userRepository.findByIdAndDeletedFalse(followingId)
                ?: throw UserNotFoundException()

            followingRepository.findByFollowerAndFollowingAndDeletedFalse(follower, following)?.let {
                throw FollowAlreadyExistsException()
            }


            var status = FollowStatus.ACCEPTED
            if (following.accountType== AccountType.PRIVATE){
                status = FollowStatus.PENDING
            }
            var follow = UserFollow(
                follower = follower,
                following = following,
                status = status
            )
            followingRepository.save(follow)
        }
    }

    @Transactional
    override fun unfollow(userFollowCreateRequest: UserFollowRequest) {
        userFollowCreateRequest.run {
            val follower = userRepository.findByIdAndDeletedFalse(followerId)
                ?: throw UserNotFoundException()

            val following = userRepository.findByIdAndDeletedFalse(followingId)
                ?: throw UserNotFoundException()

            val follow = followingRepository.findByFollowerAndFollowingAndDeletedFalse(follower, following)
                ?: throw FollowNotFoundException()
            followingRepository.trash(follow.id!!)
        }
    }

    @Transactional
    override fun confirmFollow(userFollowConfirmRequest: UserFollowConfirmRequest) {

        userFollowConfirmRequest.run {
            val follower = userRepository.findByIdAndDeletedFalse(followerId)
                ?: throw UserNotFoundException()

            val following = userRepository.findByIdAndDeletedFalse(followingId)
                ?: throw UserNotFoundException()

            val follow = followingRepository.findByFollowerAndFollowingAndDeletedFalse(follower, following)
                ?: throw FollowNotFoundException()

            if (follow.status == FollowStatus.ACCEPTED) {
                throw FollowAlreadyExistsException()
            }
            if (follow.status == FollowStatus.REJECTED){
                throw FollowAlreadyExistsException()
            }


            if (userFollowConfirmRequest.followStatus== FollowStatus.ACCEPTED){
                follow.status = FollowStatus.ACCEPTED
            }else if(userFollowConfirmRequest.followStatus== FollowStatus.REJECTED){
                follow.status = FollowStatus.REJECTED
            }

            followingRepository.save(follow)
        }
    }


    override fun existFollow(followerId: Long, followingId: Long): Boolean {
        var follower = userRepository.findByIdAndDeletedFalse(followerId)
            ?: throw UserNotFoundException()
        var following = userRepository.findByIdAndDeletedFalse(followingId)
            ?: throw UserNotFoundException()
        return followingRepository.existsByFollowerAndFollowingAndStatusAndDeletedFalse(follower, following, FollowStatus.ACCEPTED)

    }

    override fun followerByUserId(userId: Long): List<UserResponse> {
        var user = userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()
        var allFollows =
            followingRepository.findAllByFollowingAndStatusAndDeletedFalse(user, FollowStatus.ACCEPTED)
        return allFollows.map { follow->
            userMapper.toDto(follow.follower)
        }
    }

    override fun followingByUserId(userId: Long): List<UserResponse> {
        var user  = userRepository.findByIdAndDeletedFalse(userId)
            ?:throw UserNotFoundException()

        var allFollows =
            followingRepository.findAllByFollowerAndStatusAndDeletedFalse(user, FollowStatus.ACCEPTED)

        return allFollows.map {
            userMapper.toDto(it.following)
        }
    }
}