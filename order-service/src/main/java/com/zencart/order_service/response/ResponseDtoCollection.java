package com.zencart.order_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDtoCollection<T> {

    private Collection<T> collection;
}
