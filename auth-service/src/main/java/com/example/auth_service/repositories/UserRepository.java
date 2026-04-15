package com.example.auth_service.repositories;

import com.example.auth_service.models.Role;
import com.example.auth_service.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("""
            SELECT u FROM User u
            WHERE u.email = :emailOrUsername
            OR u.username = :emailOrUsername
            """)
    Optional<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    @Query("""
            SELECT u.id FROM User u
            WHERE u.email = :emailOrUsername
            OR u.username = :emailOrUsername
            """)
    Integer findUserIdByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    @Query("""
            SELECT u.role FROM User u
            WHERE u.email = :emailOrUsername
            OR u.username = :emailOrUsername
            """)
    String findUserRoleByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    @Query("""
            SELECT COUNT(u.email) > 0 FROM User u
            WHERE u.email = :email
            """)
    boolean isEmailExists(@Param("email") String email);

    @Query("""
            SELECT COUNT(u.username) > 0 FROM User u
            WHERE u.username = :username
            """)
    boolean isUsernameExists(@Param("username") String username);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE User u
            SET u.role = :role
            WHERE u.id = :userId
            """)
    void updateUserRoleByUserId(@Param("userId") Integer userId,
                                @Param("role") Role role);

    @Query("""
            SELECT u.role FROM User u
            WHERE u.id = :userId
            """)
    String findUserRoleByUserId(@Param("userId") Integer userId);
}
