package com.example.jpa.user.repository;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository public interface UserRepository extends JpaRepository<User,Long> {
    //Optional<User> findByEmail(String email);오류
    int countByEmail(String email);//36
    Optional<User> findByIdAndPassword(long id, String password);
}