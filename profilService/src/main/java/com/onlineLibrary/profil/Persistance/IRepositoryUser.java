

package com.onlineLibrary.profil.Persistance;

import com.onlineLibrary.profil.Entities.DAO.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRepositoryUser extends JpaRepository<UserDAO, Integer> {
    Optional<UserDAO> findUserByEmail(String email);
}