package com.project.un_site_de_planification_et_de_suivi_de_projets.controllers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.un_site_de_planification_et_de_suivi_de_projets.config.AESCrypter;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Reclamation;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.ReclamationDb;
import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.User;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.MyService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.ReclamationService;
import com.project.un_site_de_planification_et_de_suivi_de_projets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.un_site_de_planification_et_de_suivi_de_projets.config.AESCrypter.loadAESKey;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {
    private final MyService myService;

    private final UserService userService;

    private byte[] bytesResult;

    private  SecretKey keySecret ;

    private final ReclamationService reclamationService;

    @Autowired
    public ReclamationController(MyService myService, UserService userService, ReclamationService reclamationService) {
        this.myService = myService;
        this.userService = userService;
        this.reclamationService = reclamationService;
    }



    @PostMapping("/encrypt")
    public String encryptField(@RequestBody String plaintext) {
        // Endpoint to encrypt data
        return myService.encryptFieldWithSymmetricKey(plaintext);
    }

    @PostMapping("/decrypt")
    public String decryptField(@RequestBody String encryptedData) {
        // Endpoint to decrypt data
        return myService.decryptFieldWithSymmetricKey(encryptedData);
    }

    @PostMapping("/v2/decrypt")
    public String decryptFieldV2(@RequestBody String encryptedData) {
        // Endpoint to decrypt data
        String secretKey = "QoOtAu0+fbifx6F68uJDWMDkBTZZccNG31z4oIuwg7c=\n";
        return myService.decrypt(encryptedData, secretKey);
    }

    @PostMapping("/v3/encrypt")
    public String encryptFieldV3(@RequestBody String encryptedData) throws UnsupportedEncodingException {
        // Endpoint to decrypt data
        this.bytesResult = myService.encryptField(encryptedData, keySecret);
        System.out.println(this.bytesResult);
        String s = new String(this.bytesResult, StandardCharsets.UTF_8);
        System.out.println(s);
        return s;

    }

    @PostMapping("/v3/decrypt")
    public String decryptFieldV3( ) throws UnsupportedEncodingException {
        // Endpoint to decrypt data
         String bytes =  myService.decryptField(this.bytesResult, keySecret);
        System.out.println(bytes);

        return bytes ;
    }

    @PostMapping("/add")
    @ResponseBody
    public Reclamation addNewReclamation(@RequestBody Reclamation reclamation) throws Exception {
       AESCrypter.generateAndStoreAESKey();
        System.out.println(keySecret.getEncoded().toString());
        ReclamationDb reclamationDb = new ReclamationDb();
        reclamationDb.setClaimText(AESCrypter.encryptField(reclamation.getClaimText(), keySecret));
        reclamationDb.setClaimDate(LocalDateTime.now());
        User user = userService.findUserById(reclamation.getProviderId());
        reclamationDb.setUser(user);
         reclamationService.addReclamation(reclamationDb);
         return reclamation;
    }

    @JsonIgnore
    @GetMapping("/all")
    @ResponseBody
    public List<Reclamation> allReclamations(){
        List<ReclamationDb> reclamationDbs = reclamationService.findAllReclamations();
        List<Reclamation>   reclamations   = new ArrayList<>();
        for (ReclamationDb reclamationDb : reclamationDbs){
            reclamations.add(new Reclamation(reclamationDb.getId(), AESCrypter.decryptField(reclamationDb.getClaimText(),keySecret), reclamationDb.getClaimDate(),reclamationDb.getUser().getId()));
        }
        return reclamations;
    }


    @GetMapping("/id/{id}")
    @ResponseBody
    public Reclamation findReclamationById(@PathVariable("id") long id){
        System.out.println(keySecret.getEncoded().toString());        ReclamationDb reclamationDb = reclamationService.findReclamationById(id);
        Reclamation reclamation = new Reclamation(AESCrypter.decryptField(reclamationDb.getClaimText(), keySecret), reclamationDb.getClaimDate());
        // Set the providerId if available
        reclamation.setProviderId(reclamationDb.getUser().getId());
        return reclamation;
    }

    @GetMapping("/id/{id}/reclamationsByProvider")
    @ResponseBody
    public List<Reclamation> findReclamationByProviderId(@PathVariable("id") long id){
        System.out.println(keySecret.getEncoded().toString());        User user = userService.findUserById(id);
        List<ReclamationDb> reclamationDbs = user.getReclamations();
        List<Reclamation>   reclamations  = new ArrayList<>();
        for (ReclamationDb reclamationDb : reclamationDbs)
        reclamations.add(new Reclamation(reclamationDb.getId(), AESCrypter.decryptField(reclamationDb.getClaimText(), keySecret), reclamationDb.getClaimDate(), reclamationDb.getUser().getId()));
        return reclamations;
    }


    @PutMapping("/update")
    @ResponseBody
    public void updateReclamation(@RequestBody Reclamation reclamation){
        ReclamationService.update(reclamation); }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deleteReclamation(@PathVariable("id") long id){
        //delete all products attached with this categorie
        reclamationService.deleteBudget(id); }


  /*  @GetMapping("/id_space/{id_solution}")
    @ResponseBody
    public List<Categorie> getCategoriesByIdSolution( @PathVariable String id_solution) {
        return categorieService.getCategoriesByIdSolution( id_solution);
    }
   */
  @PostConstruct
  public void init() {
      // Charge la clé lors de l'initialisation de l'application
      keySecret = loadAESKey();
  }

}
