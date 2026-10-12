package org.dewqryp.reactive.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table
public class Taco {

    private @NonNull String name;


    private Set<Long> ingredientIds = new HashSet<>();

    @Id
    private Long id;

    public void addIngredient(Ingredient ingredient) {
        ingredientIds.add(ingredient.getId());
    }
    public void addIngredients(List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
        }
    }

}
