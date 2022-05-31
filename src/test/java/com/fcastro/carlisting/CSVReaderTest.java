package com.fcastro.carlisting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CSVReaderTest {

    @InjectMocks
    private ListingCSVReader listingCsvReader;

    @Test
    public void givenCSVFile_whenReadFile_returnListings() throws Exception {
        //given
        String content  = "code,make/model,power-in-ps,year,color,price\r"+
                "1,mercedes/a 180,123,2014,black,15950\r" +
                "2,audi/a3,111,2016,white,17210\r"+
                "3,vw/golf,86,2018,green,14980\r"+
                "4,skoda/octavia,86,2018,gray,16990\r";

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "tools/upload-error.csv",
                MediaType.TEXT_PLAIN_VALUE,
                content.getBytes(StandardCharsets.ISO_8859_1)
        );
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        //when
        List<Listing> listings = listingCsvReader.readFile(reader);

        //then
        assertThat(listings.size()).isEqualTo(4);
        assertThat(listings.get(0).getMake()).isEqualTo("mercedes");
        assertThat(listings.get(0).getModel()).isEqualTo("a 180");

    }

    @Test
    public void givenBadCSVFile_whenReadFile_returnException() throws Exception {
        //given
        String content  = "code,make/model,power-in-ps,year,color,price\r"+
                "1,mercedes/a 180,123,2014,black,15950\r" +
                "2,audi/a3,111,2016,white,17210\r"+
                "3,vw/golf,86,2018,green,14980\r"+
                "4,skoda/octavia,86,2018,16990\r";

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "tools/upload-error.csv",
                MediaType.TEXT_PLAIN_VALUE,
                content.getBytes(StandardCharsets.ISO_8859_1)
        );
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

        //when
        HttpClientErrorException thrown = Assertions.assertThrows(HttpClientErrorException.class, () -> {
            listingCsvReader.readFile(reader);
        });

        System.out.println(thrown.getMessage());
        //then
        assertThat(thrown.getMessage()).contains("Number of data fields does not match number of headers");
    }
}
