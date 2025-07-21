package com.example.Flux.Implementations;

import com.example.Persistance.Interfaces.IOrderLineRepository;
import com.google.gson.JsonObject;
import com.example.Flux.Interfaces.ISynchronizedOrderManager;
import com.example.Entities.Delivery;
import com.example.Flux.Interfaces.IDeliveryService;
import com.example.Persistance.Interfaces.IDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService implements IDeliveryService {

    private IOrderLineRepository orderLineRepository;
    private IDeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(IDeliveryRepository deliveryRepository,IOrderLineRepository orderLineRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public Delivery planifierLivraison(int orderId, JsonObject adresse) throws Exception {
         Delivery delivery = new Delivery(orderId);
         delivery.setAdresse(adresse);
         int deliveryId = deliveryRepository.save(delivery);
         delivery.setId(deliveryId);
         return delivery;
    }

    @Override
    public JsonObject deliverOrder(int orderId) {
        orderLineRepository.updateDeliveryStatusToDelivered(orderId);
        return deliveryRepository.deliverOrder(orderId);
    }

    @Override
    public JsonObject findDelivryByOrderId(int orderId) {
        return deliveryRepository.findDeliveryByOrderId(orderId);
    }

    @Override
    public void scheduleAutoDelivery(int orderId) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);

                JsonObject deliveryReport = this.deliverOrder(orderId);
                ISynchronizedOrderManager delayedResults = ISynchronizedOrderManager.init();
                synchronized (delayedResults) {
                    delayedResults.addRepport(deliveryReport);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
