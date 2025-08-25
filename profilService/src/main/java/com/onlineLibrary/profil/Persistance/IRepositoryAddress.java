
package com.onlineLibrary.profil.Persistance;

import com.onlineLibrary.profil.Entities.DAO.AddressDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IRepositoryAddress extends JpaRepository<AddressDAO, Integer> {
    @Query("SELECT a FROM AddressDAO a WHERE a.userId = :userId")
    Optional<AddressDAO> findUserProfilById(@Param("userId") int userId);
}