package com.store.promotionservice.exceptions;

public class ErrorCommunicates {
    public static String entityWithIdExists(String entityName, Object id) {
        return entityName + " with id: " + id + " already exists.";
    }

    public static String entityWithIdDoesntExist(String entityName, Object id) {
        return entityName + " with id: " + id + " doesn't exist.";
    }
}
