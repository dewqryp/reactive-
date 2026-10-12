package org.dewqryp.reactive.data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
@Table(name="TACO_ORDER")
public class TacoOrder implements Serializable {
    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;

    @Id
    private Long id;



    private Set<Long> tacoIds = new HashSet<>();





    @Transient
    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco){
        this.tacos.add(taco);
        this.tacoIds.add(taco.getId());
    }


}
