package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "departement")
@Data
public class Departement {
    @Id
    @Column(name = "code", length = 50)
    private String code;
    
    @Column(nullable = false)
    private String nom;
    
    @OneToOne
    @JoinColumn(name = "chef_departement", unique = true)
    private Employe chefDepartement;
    
    private String email;
    private String telephone;
    
    
    
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "departement")
    private List<Employe> employes = new ArrayList<>();
    

    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}