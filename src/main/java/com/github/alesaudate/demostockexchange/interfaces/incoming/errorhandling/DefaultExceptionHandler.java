package com.github.alesaudate.demostockexchange.interfaces.incoming.errorhandling;

import com.github.alesaudate.demostockexchange.domain.exceptions.StockNotMonitoredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(StockNotMonitoredException.class)
    public ResponseEntity handleStockNotMonitoredException() {
        return ResponseEntity.notFound().build();
    }

}
