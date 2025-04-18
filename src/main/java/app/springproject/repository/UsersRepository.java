package app.springproject.repository;

import app.springproject.entity.User;
import app.springproject.exception.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailEquals(String username) throws UserNotFoundException;

  Optional<User> findByNameLike(String name) throws UserNotFoundException;

  List<User> findAll();

  Optional<User> findById(Long userId);
}
