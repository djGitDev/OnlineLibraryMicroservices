package com.onlineLibrary.UtilProfil;

import com.onlineLibrary.Flux.IHashService;
import com.onlineLibrary.Flux.ILoginService;
import com.onlineLibrary.Flux.IProfilServiceDispatcher;
import com.onlineLibrary.Flux.IRegisterService;
import com.onlineLibrary.Persistance.IDBConnection;
import com.onlineLibrary.Persistance.IRepositoryAddress;
import com.onlineLibrary.Persistance.IRepositoryUser;

public interface IBeansInjectionFactory {

    IProfilServiceDispatcher getProfilServiceDispatcher();
    IRegisterService getRegisterService();
    IDBConnection getDBConnection();
    IHashService getHasherPassword();
    IRepositoryAddress getAddressRepository();
    IRepositoryUser getUserRepository();
    ILoginService getLoginService();
}
