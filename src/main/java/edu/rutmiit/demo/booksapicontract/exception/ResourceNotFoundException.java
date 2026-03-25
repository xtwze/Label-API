package edu.rutmiit.demo.booksapicontract.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Универсальное исключение для случаев, когда ресурс (артист, профиль или релиз) не найден.
 * Аннотация @ResponseStatus позволяет Spring автоматически возвращать 404 Not Found,
 * если исключение не перехвачено вручную.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s с id=%s не найден", resourceName, resourceId));
    }

    /**
     * Перегруженный конструктор для поиска по другим полям (например, по telegramUserId)
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(String.format("%s с %s='%s' не найден", resourceName, fieldName, value));
    }
}