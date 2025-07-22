package com.onlineLibrary.profil.UtilProfil;

import com.onlineLibrary.profil.Flux.IHashService;
import com.onlineLibrary.profil.Flux.ILoginService;
import com.onlineLibrary.profil.Flux.IProfilServiceDispatcher;
import com.onlineLibrary.profil.Flux.IRegisterService;
import com.onlineLibrary.profil.Persistance.IDBConnection;
import com.onlineLibrary.profil.Persistance.IRepositoryAddress;
import com.onlineLibrary.profil.Persistance.IRepositoryUser;

public interface IBeansInjectionFactory {

    IProfilServiceDispatcher getProfilServiceDispatcher();
    IRegisterService getRegisterService();
    IDBConnection getDBConnection();
    IHashService getHasherPassword();
    IRepositoryAddress getAddressRepository();
    IRepositoryUser getUserRepository();
    ILoginService getLoginService();
}
