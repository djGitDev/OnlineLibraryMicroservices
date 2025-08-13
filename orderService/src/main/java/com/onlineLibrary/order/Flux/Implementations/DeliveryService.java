package com.onlineLibrary.order.Flux.Implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineLibrary.order.Entities.DAO.DeliveryDAO;
import com.onlineLibrary.order.Entities.DTO.AddressDTO;
import com.onlineLibrary.order.Entities.DTO.DeliveryDTO;
import com.onlineLibrary.order.Entities.DTO.NotificationResponseDTO;
import com.onlineLibrary.order.Flux.Interfaces.ProfilMicroservicesClient;
import com.onlineLibrary.order.Persistance.IOrderLineRepository;
import com.onlineLibrary.order.Flux.Interfaces.IDeliveryService;
import com.onlineLibrary.order.Persistance.IDeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DeliveryService implements IDeliveryService {

    private final IOrderLineRepository orderLineRepository;
    private final IDeliveryRepository deliveryRepository;
    private final NotificationProducer notificationProducer;
    private final ProfilMicroservicesClient profilMicroservicesClient;
    private final NotificationEventBuilder notificationEventBuilder;
    private final ObjectMapper objectMapper;



    @Autowired
    public DeliveryService(IDeliveryRepository deliveryRepository,
                           IOrderLineRepository orderLineRepository,
                           NotificationProducer notificationProducer,
                           ProfilMicroservicesClient profilMicroservicesClient,
                           NotificationEventBuilder notificationEventBuilder,
                           ObjectMapper objectMapper
    ) {
        this.deliveryRepository = deliveryRepository;
        this.orderLineRepository = orderLineRepository;
        this.notificationProducer = notificationProducer;
        this.profilMicroservicesClient = profilMicroservicesClient;
        this.notificationEventBuilder = notificationEventBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public DeliveryDTO scheduleDelivery(int orderId, AddressDTO adresse) throws Exception {
        DeliveryDAO delivery = new DeliveryDAO(orderId);
        delivery.setAdresse(adresse);
        delivery = deliveryRepository.save(delivery);
        return fromDAO(delivery);
    }

    @Override
    public void deliverOrder(int userId, int orderId) {
        orderLineRepository.markAsDeliveredByOrderId(orderId);
        Optional<DeliveryDAO> deliveryOpt = deliveryRepository.findDeliveryByOrderId(orderId);
        if (deliveryOpt.isPresent()) {
            DeliveryDAO delivery = deliveryOpt.get();
            delivery.setStatut("DELIVERED");
            deliveryRepository.save(delivery);
            ResponseEntity<JsonNode> jsonUserProfil = profilMicroservicesClient.callGetUserData(userId);
            NotificationResponseDTO notificationResponseDTO = notificationEventBuilder.buildDeliveryStatusChangeNotification(delivery,jsonUserProfil.getBody());
            JsonNode notificationEventJson = objectMapper.valueToTree(notificationResponseDTO);
            notificationProducer.sendNotification(notificationEventJson.toString());
        }
    }

    @Override
    public Optional<DeliveryDTO> findDelivryByOrderId(int orderId) {
        return deliveryRepository.findDeliveryByOrderId(orderId).map(this::fromDAO);
    }

    @Override
    public void scheduleAutoDelivery(int userId, int orderId) {
        new Thread(() -> {
            try {
                Thread.sleep(30000);
                this.deliverOrder(userId, orderId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private  DeliveryDTO fromDAO(DeliveryDAO dao) {
        return new DeliveryDTO(
                dao.getId(),
                dao.getOrderId(),
                dao.getDatePrevue(),
                dao.getEffectifDate(),
                dao.getStatut(),
                dao.getStreet(),
                dao.getCity(),
                dao.getPostalCode(),
                dao.getProvince(),
                dao.getCountry()
        );
    }

}
