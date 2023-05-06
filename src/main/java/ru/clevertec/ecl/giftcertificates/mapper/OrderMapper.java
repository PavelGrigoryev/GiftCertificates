package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderDto;
import ru.clevertec.ecl.giftcertificates.model.Order;

@Mapper(componentModel = "spring", uses = GiftCertificateMapper.class ,unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDto toDto(Order order);

    Order fromDto(OrderDto orderDto);

}
