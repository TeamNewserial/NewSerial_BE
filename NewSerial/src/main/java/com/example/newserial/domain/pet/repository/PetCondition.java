package com.example.newserial.domain.pet.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pet_condition")
public class PetCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10)
    private String ranks;

    private int count;

    @OneToMany(mappedBy = "petCondition")
    private List<Pet> petList=new ArrayList<>();

}
