//package com.rosist.kardex.security.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.rosist.kardex.security.entity.UserInfo;
//
//public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
//    Optional<UserInfo> findByEmail(String email); 
//    // Use 'email' if that is the correct field for login
//}