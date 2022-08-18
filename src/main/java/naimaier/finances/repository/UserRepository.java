package naimaier.finances.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import naimaier.finances.model.User;

public interface UserRepository extends JpaRepository<User, String>{

}
