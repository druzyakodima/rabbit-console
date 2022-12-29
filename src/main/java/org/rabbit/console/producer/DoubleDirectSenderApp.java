package org.rabbit.console.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class DoubleDirectSenderApp {

    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            channel.basicPublish(EXCHANGE_NAME, "php", null, "php msg".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "c++", null, "c++ msg".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME, "java", null, "java msg".getBytes(StandardCharsets.UTF_8));

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
