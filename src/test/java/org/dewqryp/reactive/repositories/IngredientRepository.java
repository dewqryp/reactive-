package org.dewqryp.reactive.repositories;

import org.dewqryp.reactive.data.Ingredient;
import org.dewqryp.reactive.data.TacoOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IngredientRepository extends ReactiveCrudRepository<Ingredient,String> {
}
