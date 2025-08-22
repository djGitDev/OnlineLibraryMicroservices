package com.onlineLibrary.profil.Flux;

import com.onlineLibrary.profil.Entities.DAO.UserDAO;
import com.onlineLibrary.profil.Entities.DAO.AddressDAO;
import com.onlineLibrary.profil.Entities.DTO.AddressDTO;
import com.onlineLibrary.profil.Entities.DTO.RegisterRequestDTO;
import com.onlineLibrary.profil.Entities.DTO.RegisterResponseDTO;
import com.onlineLibrary.profil.Entities.DTO.UserDTO;
import com.onlineLibrary.profil.Persistance.IRepositoryAddress;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService implements IRegisterService {

    private final IRepositoryUser repositoryUser;
    private final IRepositoryAddress repositoryAddress;
    private final IHashService passwordHasher;

    @Autowired
    public RegisterService(IRepositoryUser repositoryUser,IRepositoryAddress repositoryAddress,IHashService passwordHasher) {
        this.repositoryUser = repositoryUser;
        this.repositoryAddress = repositoryAddress;
        this.passwordHasher = passwordHasher;
    }

    @Override
    @Transactional
    public RegisterResponseDTO registerUser(RegisterRequestDTO dataRequest) throws Exception {

        UserDTO userDTO = dataRequest.getUser();
        AddressDTO addressDTO = dataRequest.getAddress();

        UserDAO userDAO = convertUserDTOToDAO(userDTO);
        AddressDAO addressDAO = convertAddressDTOToDAO(addressDTO);

        userDAO.setPassword(passwordHasher.encyptPassword(userDAO.getPassword()));
        userDAO = repositoryUser.save(userDAO);
        int userId = userDAO.getId();
        String role = userDAO.getRole();

        if (userId <= 0) {
            throw new Exception("Failed to save user");
        }

        addressDAO.setUserId(userId);
        addressDAO = repositoryAddress.save(addressDAO);
        int addressId = addressDAO.getId();

        if (addressId <= 0) {
            throw new Exception("Failed to save address");
        }

        return new RegisterResponseDTO("success", role, userId, addressId);
    }

    private UserDAO convertUserDTOToDAO(UserDTO dto) {
        UserDAO userDAO = new UserDAO();
        userDAO.setFirstName(dto.getFirstName());
        userDAO.setLastName(dto.getLastName());
        userDAO.setEmail(dto.getEmail());
        userDAO.setPhone(dto.getPhone());
        userDAO.setPassword(dto.getPassword());// pense à hasher après la conversion
        userDAO.setRole(dto.getRole());
        return userDAO;
    }

    private AddressDAO convertAddressDTOToDAO(AddressDTO dto) {
        AddressDAO addressDAO = new AddressDAO();
        addressDAO.setStreet(dto.getStreet());
        addressDAO.setCity(dto.getCity());
        addressDAO.setPostalCode(dto.getPostalCode());
        addressDAO.setProvince(dto.getProvince());
        addressDAO.setCountry(dto.getCountry());
        return addressDAO;
    }

}