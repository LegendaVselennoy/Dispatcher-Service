package com.polarbookshop.dispatcherservice;

// DTO, содержащий идентификатор ордера в виде поля Long
public record OrderAcceptedMessage(
        Long orderId
) {}
