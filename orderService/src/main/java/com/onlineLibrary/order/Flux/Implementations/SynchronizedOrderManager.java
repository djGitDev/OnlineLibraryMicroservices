package com.onlineLibrary.order.Flux.Implementations;

import com.onlineLibrary.order.Flux.Interfaces.ISynchronizedOrderManager;
import com.google.gson.JsonObject;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class SynchronizedOrderManager implements ISynchronizedOrderManager {

    private static final SynchronizedOrderManager INSTANCE = new SynchronizedOrderManager();
    private final List<JsonObject> synchronizedOrders;

    public SynchronizedOrderManager() {
        this.synchronizedOrders = Collections.synchronizedList(new ArrayList<>());
    }
    // Méthode pour récupérer l'instance Singleton
    public static SynchronizedOrderManager getInstance() {
        return INSTANCE;
    }
    // add order thread-safetly
    @Override
    public void addRepport(JsonObject order) {
        synchronized (synchronizedOrders) {
            synchronizedOrders.add(order);
        }
    }
    // fetch all orders thread-safetly
    @Override
    public List<JsonObject> getAllRepports() {
        synchronized (synchronizedOrders) {
            return new ArrayList<>(synchronizedOrders);
        }
    }

    // clean list thread-safetly
    @Override
    public void clearRepports() {
        synchronized (synchronizedOrders) {
            synchronizedOrders.clear();
        }
    }
}

