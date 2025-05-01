package com.uchk.university.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import com.uchk.university.entity.Student;
import com.uchk.university.entity.User;

@Entity
@Table(name = "formations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;
    private String level;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private BigDecimal fundingAmount;
    private String fundingType;

    @OneToMany(mappedBy = "currentFormation")
    private List<Student> students;

    @ManyToMany
    @JoinTable(
        name = "formation_staff",
        joinColumns = @JoinColumn(name = "formation_id"),
        inverseJoinColumns = @JoinColumn(name = "staff_id")
    )
    private List<User> staff;
}