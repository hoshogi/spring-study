package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username을 기준으로 User 정보를 가져올 때 권한 정보도 같이 가져오는 메서드
    // @EntityGraph은 쿼리가 수행될 때 Lazy 조회가 아니고 Eager 조회로 authorities  정보를 같이 가져온다
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}