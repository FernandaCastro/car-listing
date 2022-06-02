package com.fcastro.carlisting;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListingCSV {

    @CsvBindByName(required = true)
    private String code;

    @CsvBindAndSplitByName(required = true, elementType = String.class, collectionType = List.class, splitOn = "/", column = "make/model")
    private List<String> makeModel;

    @CsvBindByName(required = true, column = "power-in-ps")
    private int kw;

    @CsvBindByName(required = true)
    private int year;

    @CsvBindByName(required = true)
    private String color;

    @CsvBindByName(required = true)
    private BigDecimal price;
}
