package com.study.springbatch.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Coffee {

    private String brand;
    private String origin;
    private String characteristics;
    
    public Coffee() {
	}

    public Coffee(String brand, String origin, String characteristics) {
        this.brand = brand;
        this.origin = origin;
        this.characteristics = characteristics;
    }
    
    @Override
    public String toString() {
        return "Coffee [brand=" + getBrand() + ", origin=" + getOrigin() + ", characteristics=" + getCharacteristics() + "]";
    }
}
