package com.fcastro.carlisting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers=ListingController.class)
public class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;

    @Test
    public void givenCSVFile_whenUploadCSV_shouldReturnSuccess() throws Exception {
        //given
        Listing listing1 = Listing.builder().id(1L).dealerId(1L).code("1").make("mercedes").model("a 180").kw(180).year(2014).color("black").price(BigDecimal.valueOf(15950)).build();
        Listing listing2 = Listing.builder().id(2L).dealerId(1L).code("2").make("audi").model("a3").kw(111).year(2016).color("white").price(BigDecimal.valueOf(17210)).build();

        given(listingService.processFile(anyLong(), any(BufferedReader.class))).willReturn(Arrays.asList(listing1, listing2));

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "tools/upload-error.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "content".getBytes()
        );

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/car-listing/upload/1")
                        .file(file))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].make", is("mercedes")))
                .andExpect(jsonPath("$[0].model", is("a 180")))
                .andExpect(jsonPath("$[0].id", is(1)));
    }
}
