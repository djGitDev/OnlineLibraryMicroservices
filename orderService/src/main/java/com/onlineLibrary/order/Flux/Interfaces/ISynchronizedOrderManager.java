
package com.onlineLibrary.order.Flux.Interfaces;

import com.onlineLibrary.order.Flux.Implementations.SynchronizedOrderManager;
import com.google.gson.JsonObject;
import java.util.List;


public interface ISynchronizedOrderManager {

    static ISynchronizedOrderManager init() {
        return new SynchronizedOrderManager();
    }
    void addRepport(JsonObject order);
    List<JsonObject> getAllRepports();
    void clearRepports();
}
