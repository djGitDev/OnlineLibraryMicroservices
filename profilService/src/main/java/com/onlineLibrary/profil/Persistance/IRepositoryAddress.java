
package com.onlineLibrary.profil.Persistance;

import com.onlineLibrary.profil.Entities.DAO.AddressDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IRepositoryAddress extends JpaRepository<AddressDAO, Integer> {
    Optional<AddressDAO> findUserProfilById(int userId);
}