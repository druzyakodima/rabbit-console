package org.rabbit.console.consumer;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DoubleDirectReceiver {

    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String topic = scanner.nextLine();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String queueName = channel.queueDeclare().getQueue();
        System.out.println("My queue name: " + queueName);

        if (topic.matches("(.*)php(.*)")) {
            channel.queueBind(queueName, EXCHANGE_NAME, "php");
        } else if (topic.matches("(.*)java(.*)")) {
            channel.queueBind(queueName, EXCHANGE_NAME, "java");
        } else if (topic.matches("(.*)c++(.*)")) {
            channel.queueBind(queueName, EXCHANGE_NAME, "c++");
        }

        System.out.println("[*] Waiting for message");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[x] Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
