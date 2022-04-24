package com.cos.security1.Repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JPA repository가 들고있다.
// @repository라는 어노테이션 없어도 Ioc됨. 이유는 hparepository를 상속했기 때문에
// JpaRepository 를 상속하면 자동 컴포넌트 스캔됨.
public interface UserRepository extends JpaRepository<User, Integer> {

    // Jpa Naming 전략
    // SELECT * FROM user WHERE username = 1?
//    User findByUsername(String username);
    // SELECT * FROM user WHERE username = 1? AND password = 2?
    // User findByUsernameAndPassword(String username, String password);

    // @Query(value = "select * from user", nativeQuery = true)
    // User find마음대로();
}