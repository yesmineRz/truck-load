package com.metro.truckload.controller;

import com.metro.truckload.dto.request.GetOptimalTruckLoadRequestDto;
import com.metro.truckload.dto.response.GetOptimalTruckLoadResponseDto;
import com.metro.truckload.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoadController {

    @Autowired
    private LoadService loadService;

    @PostMapping("/optimal-load")
    public GetOptimalTruckLoadResponseDto getOptimalTruckLoad(@Valid @RequestBody GetOptimalTruckLoadRequestDto dtoRequest){
        return loadService.getOptimalTruckLoad(dtoRequest.weights, dtoRequest.numberOfTrucks);
    }

}
