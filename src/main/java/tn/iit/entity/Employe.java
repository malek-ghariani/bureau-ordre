package tn.iit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "employe")
@Data
public class Employe implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String matricule;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String telephone;

    // Le poste sera un enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PosteEmploye poste;

    @ManyToOne
    @JoinColumn(name = "departement_code")
    private Departement departement;

    // Le rôle sera un enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEmploye role = RoleEmploye.AGENT;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "employe")
    private List<CourrierEntrant> courriersEntrantsCrees = new ArrayList<>();

    @OneToMany(mappedBy = "employe")
    private List<CourrierSortant> courriersSortantsCrees = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Utilise l'enum RoleEmploye pour générer l'autorité
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
