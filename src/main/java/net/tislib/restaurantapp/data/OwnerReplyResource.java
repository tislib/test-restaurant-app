package net.tislib.restaurantapp.data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

public class OwnerReplyResource {

    @NotBlank
    private String ownerReply;

    private Instant replyTime;

}
