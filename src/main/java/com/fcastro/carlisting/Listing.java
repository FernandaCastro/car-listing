package com.fcastro.carlisting;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name= "listing", uniqueConstraints = { @UniqueConstraint(name = "UniqueDealerIdAndCode", columnNames = { "dealer_Id", "code" })})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dealer_id", nullable = false)
    private Long dealerId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "kw", nullable = false)
    private int kw;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

}
