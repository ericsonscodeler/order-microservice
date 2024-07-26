package com.ericson.order_microservice.controller.dto;

import java.util.List;

public record ApiResponseDTO<T>(List<T> data, PaginationResponseDTO pagination) {

}
