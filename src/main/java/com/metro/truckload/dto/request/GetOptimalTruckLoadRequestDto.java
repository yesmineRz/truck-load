package com.metro.truckload.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class GetOptimalTruckLoadRequestDto {

    @NotNull
    @Min(value = 0)
    public Integer numberOfTrucks;

    @NotNull
    @Size(min=1)
    public int[] weights;

}
