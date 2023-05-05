package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.giftcertificates.dto.OrderDto;
import ru.clevertec.ecl.giftcertificates.model.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDto toDto(Order order);

    Order fromDto(OrderDto orderDto);

}
