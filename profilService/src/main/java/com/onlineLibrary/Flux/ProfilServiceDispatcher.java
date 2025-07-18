package com.onlineLibrary.Flux;

import com.google.gson.JsonObject;
import com.onlineLibrary.Persistance.IRepositoryAddress;
import com.onlineLibrary.Persistance.IRepositoryUser;
import com.onlineLibrary.UtilProfil.IBeansInjectionFactory;

public class ProfilServiceDispatcher implements IProfilServiceDispatcher{

    private IRegisterService registerService;
    private ILoginService loginService;
    private IRepositoryAddress repositoryAddress;
    private IRepositoryUser repositoryUser;


    public ProfilServiceDispatcher(IBeansInjectionFactory factory) {
        this.registerService = factory.getRegisterService();
        this.loginService = factory.getLoginService();
        this.repositoryAddress = factory.getAddressRepository();
        this.repositoryUser = factory.getUserRepository();
    }

    @Override
    public JsonObject handleRegistration(JsonObject data) throws Exception {
        return registerService.registerUser(data);
    }

    @Override
    public JsonObject handleLogin(JsonObject data) throws Exception {
        return loginService.authentifyUser(data);
    }

    @Override
    public JsonObject getProfile(int userId) throws Exception {
        return repositoryAddress.findUserProfilById(userId);
    }

    @Override
    public JsonObject getUserData(int userId) throws Exception {
        return repositoryUser.findUserDataById(userId);
    }
}
