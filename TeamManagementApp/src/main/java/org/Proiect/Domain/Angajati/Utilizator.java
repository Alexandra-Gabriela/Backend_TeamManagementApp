package org.Proiect.Domain.Angajati;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Proiect.Notificare;
import org.Proiect.Domain.Dezvoltare.Curs;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Domain.Proiect.Task;


import java.util.List;
@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor

public class Utilizator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "Numele este obligatoriu.")
    @Size(max = 100, message = "Numele nu poate depăși 100 de caractere.")
    private String nume;

    @NotBlank(message = "Emailul este obligatoriu.")
    @Email(message = "Adresa de email trebuie să fie validă.")
    private String email;

    @Enumerated(EnumType.STRING)
    private TipUtilizator tipUtilizator;
    @Column(name = "depasiriDeadline", nullable = false)
    private int depasiriDeadline = 0;
    private String rol;
    @Column(nullable = false)
    private boolean disponibil = true; // Valoare implicită: disponibil
    // Relații comune
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notificare> notificari;

    @ManyToMany
    @JoinTable(name = "UserCurs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "curs_id"))
    private List<Curs> cursuri;

    @ManyToOne
    @JoinColumn(name = "id_departament")
    private Departament departament;

    @ManyToOne
    @JoinColumn(name = "id_echipa")
    private Echipa echipa;

    @OneToMany(mappedBy = "lider")
    private List<Proiect> proiecte;

    @OneToMany(mappedBy = "lider", cascade = CascadeType.ALL)
    private List<Task> taskuriLider;

    @OneToMany(mappedBy = "membru", cascade = CascadeType.ALL)
    private List<Task> taskuriMembru;
    public boolean esteAdmin() {
        return this.tipUtilizator == TipUtilizator.ADMIN;
    }
    public boolean esteMembruEchipa() {
        return this.tipUtilizator == TipUtilizator.MEMBRUECHIPA;
    }
    public boolean esteLiderEchipa() {
        return this.tipUtilizator == TipUtilizator.LIDER;
    }
    public boolean esteManagerProiect() {
        return this.tipUtilizator == TipUtilizator.MANAGER;
    }
    public boolean isDisponibil() {
        return this.disponibil;  // Asumând că atributul disponibil există
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "userId=" + userId +
                ", nume='" + nume + '\'' +
                ", email='" + email + '\'' +
                ", tipUtilizator=" + tipUtilizator +
                ", depasiriDeadline=" + depasiriDeadline +
                ", rol='" + rol + '\'' +
                ", disponibil=" + disponibil +
                '}';
    }

    public UtilizatorDTO toDTO() {
        UtilizatorDTO dto = new UtilizatorDTO();
        dto.setUserId(this.userId);
        dto.setNume(this.nume);
        dto.setTipUtilizator(this.tipUtilizator);
        dto.setEmail(this.email);
        return dto;
    }

    public static Utilizator fromDTO(UtilizatorDTO dto) {
        Utilizator utilizator = new Utilizator();
        utilizator.setUserId(dto.getUserId());
        utilizator.setNume(dto.getNume());
        utilizator.setTipUtilizator(dto.getTipUtilizator());
        utilizator.setEmail(dto.getEmail());
        return utilizator;
    }

}
