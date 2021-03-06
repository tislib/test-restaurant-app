package net.tislib.restaurantapp.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Restaurant {

    private Long id;

    private String name;
}
