package com.arsen.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "payment_history")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "amount")
    private double amount;

    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "id")
    private Person person;

    public PaymentHistory(double amount, Person person) {
        this.amount = amount;
        this.person = person;
    }
}
