package com.onlineLibrary.profil.Entities.DTO;



public class RegisterRequestDTO {

    private UserDTO user;
    private AddressDTO address;

    public RegisterRequestDTO(UserDTO user, AddressDTO address) {
        this.user = user;
        this.address = address;
    }


    public UserDTO getUser() {
        return user;
    }
    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AddressDTO getAddress() {
        return address;
    }
    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
