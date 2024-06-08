package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Import(TestChannelBinderConfiguration.class) // Настройка тестового связующего
public class FunctionsStreamIntegrationTests {

    @Autowired
    //@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private InputDestination input;  // Представляет входную привязку packlabel-in-0

    @Autowired
    private OutputDestination output; // Представляет выходную привязку packlabel-out-0

    @Autowired
    private ObjectMapper objectMapper; // Использует Jackson для десериализации полезных данных сообщений JSON в объекты Java

    @Test
    void whenOrderAcceptedThenDispatched() throws IOException {
        long orderId = 121;
        Message<OrderAcceptedMessage> inputMessage = MessageBuilder
                .withPayload(new OrderAcceptedMessage(orderId)).build();
        Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder
                .withPayload(new OrderDispatchedMessage(orderId)).build();

        this.input.send(inputMessage); // Отправляет сообщение на входной канал
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
                .isEqualTo(expectedOutputMessage.getPayload());  // Получает и подтверждает сообщение из выходного канала
    }

}
