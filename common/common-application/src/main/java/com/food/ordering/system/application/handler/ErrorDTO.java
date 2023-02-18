package com.food.ordering.system.application.handler;

import org.springframework.http.HttpStatus;

public record ErrorDTO(HttpStatus code, String message) {

}
