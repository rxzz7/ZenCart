package com.zencart.user_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
//wrapper class for data consistency and for metadata expansion
public class ResponseCollectionDTO<T> {

    private Collection<T> collection;

}
