package com.project.un_site_de_planification_et_de_suivi_de_projets.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String claimText;
    private LocalDateTime claimDate;

    private Long providerId;

    public Reclamation(String claimText, LocalDateTime claimDate) {
        this.claimText = claimText;
        this.claimDate = claimDate;
    }

    public Reclamation(Long id, String claimText, LocalDateTime claimDate, Long providerId) {
        this.id = id;
        this.claimText = claimText;
        this.claimDate = claimDate;
        this.providerId = providerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimText() {
        return claimText;
    }

    public void setClaimText(String claimText) {
        this.claimText = claimText;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }
}
