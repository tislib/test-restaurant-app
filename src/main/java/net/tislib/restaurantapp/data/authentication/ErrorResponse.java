package net.tislib.restaurantapp.data.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class ErrorResponse {
    private final String message;

    private Set<FieldError> rejectedFields;

    @Data
    @Builder
    public static class FieldError {
        private String name;
        private String message;
    }
}
