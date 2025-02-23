package com.example.whatsapp_clone.v1.user;

import com.example.whatsapp_clone.v1.user.model.entities.User;
import com.example.whatsapp_clone.v1.user.model.entities.UserConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(name = UserConstants.FIND_USER_BY_EMAIL)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(name = UserConstants.FIND_ALL_USERS_EXCEPT_SELF)
    Optional<User> findAllUsersExceptSelf(@Param("publicId") String publicId);

    @Query(name = UserConstants.FIND_USER_BY_PUBLIC_ID)
    Optional<User> findByPublicId(@Param("publicId") String publicId);
}
