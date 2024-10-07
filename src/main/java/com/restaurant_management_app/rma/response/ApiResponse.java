package com.restaurant_management_app.rma.response;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ApiResponse {
    private String message;
    private Object data;
}
