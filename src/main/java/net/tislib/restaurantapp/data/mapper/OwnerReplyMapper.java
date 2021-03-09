package net.tislib.restaurantapp.data.mapper;

import net.tislib.restaurantapp.data.OwnerReplyResource;
import net.tislib.restaurantapp.model.OwnerReply;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ReviewMapper.class)
public interface OwnerReplyMapper extends ResourceEntityMapper<OwnerReplyResource, OwnerReply> {

}
