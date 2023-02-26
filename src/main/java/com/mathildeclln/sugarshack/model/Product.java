package com.mathildeclln.sugarshack.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @EqualsAndHashCode
public class Product {
    @Id
    private String id;
    @Nullable
    private String name;
    @Nullable
    private String description;
    @Nullable
    private String image;
    @Nullable
    private double price;
    @Nullable
    private MapleType type;

}
