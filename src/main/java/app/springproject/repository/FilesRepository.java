package app.springproject.repository;

import app.springproject.entity.File;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {
  Optional<File> findByNameEquals(String name);
}
