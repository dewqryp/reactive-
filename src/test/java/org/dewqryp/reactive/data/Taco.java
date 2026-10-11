package org.dewqryp.reactive.data;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco {

    private String name;

    @ManyToMany
    private List<Ingredient> ingredients;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date createdAt =  new Date();
}
