package com.onlineLibrary.order.Flux.Implementations;

import com.onlineLibrary.order.Flux.Interfaces.ISynchronizedOrderManager;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class SynchronizedOrderManager implements ISynchronizedOrderManager {
    // Instance Singleton
    private static final SynchronizedOrderManager INSTANCE = new SynchronizedOrderManager();

    // Liste synchronisée de JsonObject
    private final List<JsonObject> synchronizedOrders;

    // Constructeur privé pour empêcher l'instanciation externe
    public SynchronizedOrderManager() {
        this.synchronizedOrders = Collections.synchronizedList(new ArrayList<>());
    }
    // Méthode pour récupérer l'instance Singleton
    public static SynchronizedOrderManager getInstance() {
        return INSTANCE;
    }
    // Ajouter un ordre de manière thread-safe
    @Override
    public void addRepport(JsonObject order) {
        synchronized (synchronizedOrders) {
            synchronizedOrders.add(order);
        }
    }
    // Récupérer tous les ordres de manière thread-safe
    @Override
    public List<JsonObject> getAllRepports() {
        synchronized (synchronizedOrders) {
            return new ArrayList<>(synchronizedOrders); // Retourne une copie pour éviter les modifications externes
        }
    }

    // Vider la liste de manière thread-safe
    @Override
    public void clearRepports() {
        synchronized (synchronizedOrders) {
            synchronizedOrders.clear();
        }
    }
}

