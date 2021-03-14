package net.tislib.restaurantapp.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Restaurant restaurant;

    private short starCount;

    private String comment;

    private Instant reviewTime;

    private LocalDate dateOfVisit;

    @OneToOne(mappedBy = "review")
    private OwnerReply ownerReply;

    private boolean computed;
}
