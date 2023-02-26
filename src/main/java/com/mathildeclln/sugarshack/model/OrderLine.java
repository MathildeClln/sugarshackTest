package com.mathildeclln.sugarshack.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Builder
public class OrderLine {
    @Id
    private String productId;
    @Nullable
    private int qty;


}
