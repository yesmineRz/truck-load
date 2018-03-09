package com.metro.truckload.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.metro.truckload.dto.request.GetOptimalTruckLoadRequestDto;
import com.metro.truckload.dto.response.GetOptimalTruckLoadResponseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GetOptimalTruckLoadTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private String convertJsonToString(GetOptimalTruckLoadRequestDto request) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(request);
        return requestJson;
    }

    @Test
    public void getWithMissingWeightsShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWithMissingNumberOfTrucksShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.weights = new int[]{1, 2, 3, 4};
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWithWeightsIsNullShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = null;
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWithNumberOfTrucksIsNullShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = null;
        requestDto.weights = new int[]{1, 2, 3, 4};
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWithWeightsEmptyIsNullShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[0];
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWitNumberOfTrucksNegativeShouldReturnBadRequest() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = -3;
        requestDto.weights = new int[]{1, 2, 3, 4};
        String requestString = convertJsonToString(requestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase0() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{1, 2, 3, 4};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(1, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase1() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 1;
        requestDto.weights = new int[]{1, 2, 3, 4, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase2() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{1, 2, 3, 4, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase3() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 10;
        requestDto.weights = new int[]{3};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(3, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase4() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 10;
        requestDto.weights = new int[]{1, 2, 3, 4, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(5, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase5() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 2;
        requestDto.weights = new int[]{2, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(3, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase6() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 2;
        requestDto.weights = new int[]{2, 3, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase7() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 2;
        requestDto.weights = new int[]{2, 2, 8};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(4, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase8() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{2, 3, 5};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(3, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase9() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 2;
        requestDto.weights = new int[]{4, 5, 6, 7, 8};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase10() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{2, 5, 6, 7, 8, 14};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase11() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{2, 5, 5, 8, 10, 12, 18, 19, 20};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(0, body.optimalDifference);
    }

    @Test
    public void getWithValidParamsShouldReturn200AndCorrectResultTestCase12() throws Exception {

        GetOptimalTruckLoadRequestDto requestDto = new GetOptimalTruckLoadRequestDto();
        requestDto.numberOfTrucks = 3;
        requestDto.weights = new int[]{2, 5, 5, 8, 10, 12, 18, 19, 21};
        String requestString = convertJsonToString(requestDto);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/optimal-load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(requestString)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        GetOptimalTruckLoadResponseDto body = mapper.readValue(result.getResponse().getContentAsString(), GetOptimalTruckLoadResponseDto.class);

        Assert.assertEquals(1, body.optimalDifference);
    }
}