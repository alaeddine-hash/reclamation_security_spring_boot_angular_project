package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Reclamation;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ReclamationDb;
import com.project.un_site_de_planification_et_de_suivi_de_projets.exception.UserNotFoundException;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.ReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    @Autowired
    public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;

    }

    public static void update(Reclamation reclamation) {
    }

    public ReclamationDb addReclamation(ReclamationDb reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public List<ReclamationDb> findAllReclamations() {
        return reclamationRepository.findAll();
    }

    public ReclamationDb updateReclamation(ReclamationDb reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public ReclamationDb findReclamationById(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Reclamation by id : " + id + " was not found"));
    }

    public void deleteBudget(Long id){
        reclamationRepository.deleteById(id);
    }
}
