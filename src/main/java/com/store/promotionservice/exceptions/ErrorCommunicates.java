package com.store.promotionservice.exceptions;

public class ErrorCommunicates {
    public static String entityWithIdExists(String entityName, Object id) {
        return entityName + " with id: " + id + " already exists.";
    }
}
