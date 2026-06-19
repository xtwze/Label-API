package edu.rutmiit.demo.labelrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа сервиса-реализации REST-контракта Label API.
 *
 * Сервис подключает {@code books-api-contract} как Maven-зависимость:
 * все интерфейсы (ArtistApi, ReleaseApi) и DTO приходят оттуда,
 * здесь реализуется только бизнес-логика (storage, service, controller, assembler).
 */
@SpringBootApplication
public class DemoRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoRestApplication.class, args);
    }
}
