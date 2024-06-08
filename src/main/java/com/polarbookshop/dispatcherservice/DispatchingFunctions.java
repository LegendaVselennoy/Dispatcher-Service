package com.polarbookshop.dispatcherservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

// Функции определяются в конфигурационном классе
@Configuration
public class DispatchingFunctions {

    private static final Logger log= LoggerFactory.getLogger(DispatchingFunctions.class);

    // Функции, определенные как компоненты, могут быть обнаружены и управляться с помощью Spring Cloud Function
    @Bean
    public Function<OrderAcceptedMessage,Long> pack(){  // Функция, реализующая бизнес-логику упаковки заказов
        return orderAcceptedMessage -> {           // В качестве входных данных он принимает объект OrderAcceptedMessage
            log.info("Заказ с id {} упакован.",orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId(); // Возвращает идентификатор ордера (Long) в качестве выходных данных
        };
    }

    // Функция, реализующая бизнес-логику маркировки заказов
    @Bean
    public Function<Flux<Long>,Flux<OrderAcceptedMessage>>label(){
        return orderFlux-> orderFlux.map(orderId->{ // В качестве входных данных он принимает идентификатор ордера (Long)
            log.info("Заказ с id {} помечен.",orderId);
            return new OrderAcceptedMessage(orderId); // Возвращает OrderDispatchedMessage в качестве выходных данных
        });
    }
}
