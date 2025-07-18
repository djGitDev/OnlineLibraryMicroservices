package com.onlineLibrary.UtilProfil;


import com.onlineLibrary.Flux.*;
import com.onlineLibrary.Persistance.*;

public class BeansInjectionFactory implements IBeansInjectionFactory {

    private static BeansInjectionFactory instance;

    private BeansInjectionFactory() {
    }

    public static synchronized BeansInjectionFactory getInstance() {
        if (instance == null) {
            instance = new BeansInjectionFactory();
        }
        return instance;
    }


    @Override
    public IProfilServiceDispatcher getProfilServiceDispatcher() {
        return new ProfilServiceDispatcher(this.getInstance());
    }

    @Override
    public IRegisterService getRegisterService() {
        return new RegisterService(this.getInstance());
    }

    @Override
    public IRepositoryUser getUserRepository() {
        return new RepositoryUser(this.getInstance());
    }

    @Override
    public ILoginService getLoginService() {
        return new LoginService(this.getInstance());
    }

    @Override
    public IDBConnection getDBConnection() {
        return new PostgresDBConnection();
    }

    @Override
    public IHashService getHasherPassword() {
        return new HashService();
    }

    @Override
    public IRepositoryAddress getAddressRepository() {
        return new RepositoryAddress(this.getInstance());
    }


}
