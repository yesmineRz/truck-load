package com.metro.truckload.service;

import com.metro.truckload.dto.response.GetOptimalTruckLoadResponseDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.IntStream;

@Service
public class LoadService {

    public GetOptimalTruckLoadResponseDto getOptimalTruckLoad(int[] weights, int numberOfTrucks) {

        int n = weights.length;

        int[] averageOfWeights = new int[numberOfTrucks];
        int sumOfWeights = IntStream.of(weights).sum();
        Arrays.fill(averageOfWeights, sumOfWeights / numberOfTrucks);
        int borrowed = sumOfWeights % numberOfTrucks;
        int counter = 0;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] > sumOfWeights / numberOfTrucks) {
                averageOfWeights[counter] = weights[i];
                averageOfWeights[numberOfTrucks - 1 - counter] = averageOfWeights[numberOfTrucks - 1 - counter] - (weights[i] - sumOfWeights / numberOfTrucks) + borrowed;
                borrowed = 0;
                counter++;
            }
        }
        averageOfWeights[0] += borrowed;

        int[] indexArray = new int[n];

        partitionArrayIntoAnotherProportionally(weights, n - 1, averageOfWeights, indexArray, numberOfTrucks);

        int[] partition = new int[numberOfTrucks];
        for (int i = 0; i < numberOfTrucks; i++) {
            for (int j = 0; j < n; j++) {
                if (indexArray[j] == i + 1) {
                    partition[i] += weights[j];
                }

            }
        }

        GetOptimalTruckLoadResponseDto response = new GetOptimalTruckLoadResponseDto();
        response.optimalDifference = findMaximalValue(partition) - findMinimalValue(partition);
        return response;

    }

    private static int partitionArrayIntoAnotherProportionally(int T[], int n, int remainingSum[], int indexArray[], int newPartitionSize) {
        int result = IntStream.of(remainingSum).sum();

        if (result == 0)
            return 0;

        for (int i = 0; i < newPartitionSize; i++) {
            if (result > 0 && (remainingSum[i] - T[n]) >= 0) {
                indexArray[n] = i + 1;
                remainingSum[i] = remainingSum[i] - T[n];
                result = partitionArrayIntoAnotherProportionally(T, n - 1, remainingSum, indexArray, newPartitionSize);
                remainingSum[i] = remainingSum[i] + T[n];
            }
        }

        return result;
    }

    int findMaximalValue(int[] t) {
        int maxValue = t[0];
        for (int i = 1; i < t.length; i++) {
            if (t[i] > maxValue) {
                maxValue = t[i];
            }
        }
        return maxValue;
    }

    int findMinimalValue(int[] t) {
        int minValue = t[0];
        for (int i = 1; i < t.length; i++) {
            if (t[i] < minValue) {
                minValue = t[i];
            }
        }
        return minValue;
    }
}