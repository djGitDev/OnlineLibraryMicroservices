package com.onlineLibrary.orchestre.Util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WorkFlowStateManager {


    private static final List<Integer> userIds = new ArrayList<>();
    private static final List<Integer> orderIds = new ArrayList<>();
    private static final Map<Integer, Double> searchedBooksIdsWithPrice = new HashMap<>();
    private static Integer currentCartId = 0;

    public  int getLastUserId() throws Exception {
        if (userIds.isEmpty())
            throw new Exception("Aucun utilisateur connecté.");
        return userIds.get(userIds.size() - 1);
    }

    public  int getLastOrderId() throws Exception {
        if (orderIds.isEmpty())
            throw new Exception("Aucune commande enregistrée.");
        return orderIds.get(orderIds.size() - 1);
    }

    public Map<Integer, Double> getSearchedBooksIdsWithPrice() {
        return searchedBooksIdsWithPrice;
    }

    public Integer getCurrentCartId() {
        return currentCartId;
    }

    public void addUserId(int userId){
        userIds.add(userId);
    }

    public void addOrderId(int orderId){
        orderIds.add(orderId);
    }

    public void addSearchedBooksId(int bookId, double price) {
        searchedBooksIdsWithPrice.put(bookId, price);
    }

    public void setCurrentCartId(Integer currentCartId){
        this.currentCartId = currentCartId;
    }




}
