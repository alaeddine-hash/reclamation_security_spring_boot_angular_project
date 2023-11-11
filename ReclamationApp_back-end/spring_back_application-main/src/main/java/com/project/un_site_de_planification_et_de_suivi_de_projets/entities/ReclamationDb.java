package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReclamationDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] claimText;
    private LocalDateTime claimDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ReclamationDb(byte[] claimText, LocalDateTime claimDate) {
        this.claimText = claimText;
        this.claimDate = claimDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getClaimText() {
        return claimText;
    }

    public void setClaimText(byte[] claimText) {
        this.claimText = claimText;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
