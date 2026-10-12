package org.dewqryp.reactive.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@Data
@RequiredArgsConstructor
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Ingredient {
    @Id
    private Long id;

    private @NonNull String slug;

    private @NonNull String name;

    private @NonNull Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }


}