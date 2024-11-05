package belaquaa.userserviceadmin.repository;

import belaquaa.userserviceadmin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    List<User> findAllByIsDeletedFalse();

    List<User> findAll();
}