package net.tislib.restaurantapp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InvalidFieldException extends RuntimeException {

    private final long serialVersionUID = 8439587092791L;

    private final String fieldName;
    private final String message;
}
