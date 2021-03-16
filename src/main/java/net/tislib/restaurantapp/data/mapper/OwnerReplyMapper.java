package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.model.OwnerReply;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = ReviewMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OwnerReplyMapper extends ResourceEntityMapper<OwnerReplyResource, OwnerReply> {

}
