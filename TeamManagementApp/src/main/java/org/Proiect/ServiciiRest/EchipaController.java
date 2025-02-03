package org.Proiect.ServiciiRest;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.Proiect.DTO.EchipaDTO;
import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Servicii.IEchipaWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/echipe")
@Transactional
public class EchipaController {

    private final IEchipaWorkflowService workflowService;

    public EchipaController(IEchipaWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    // Creare echipă
    @PostMapping
    public ResponseEntity<EchipaDTO> creeazaEchipa(@Valid @RequestBody EchipaDTO echipaDTO) {
        var echipa = workflowService.creeazaEchipa(echipaDTO.getDenumire(), echipaDTO.getIdEchipa());
        var rezultatDTO = new EchipaDTO();
        rezultatDTO.setIdEchipa(echipa.getIdEchipa());
        rezultatDTO.setDenumire(echipa.getDenumire());
        return ResponseEntity.ok(rezultatDTO);
    }

    // Adăugare utilizator în echipă
    @PostMapping("/{echipaId}/utilizatori")
    public ResponseEntity<String> adaugaUtilizator(@PathVariable Integer echipaId, @RequestParam Integer userId) {
        workflowService.adaugaMembruInEchipa(echipaId, userId);
        return ResponseEntity.ok("Utilizator adăugat în echipă!");
    }

    // Vizualizare toate echipele
    @GetMapping
    public ResponseEntity<List<EchipaDTO>> getAllEchipe() {
        var echipe = workflowService.getAllEchipe() // Asumăm o metodă nouă în workflow service
                .stream()
                .map(echipa -> {
                    var dto = new EchipaDTO();
                    dto.setIdEchipa(echipa.getIdEchipa());
                    dto.setDenumire(echipa.getDenumire());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(echipe);
    }

    // Vizualizare membri echipă
    @GetMapping("/{echipaId}/utilizatori")
    public ResponseEntity<List<UtilizatorDTO>> getMembriiEchipa(@PathVariable Integer echipaId) {
        var utilizatori = workflowService.vizualizeazaMembriiEchipa(echipaId)
                .stream()
                .map(utilizator -> {
                    var dto = new UtilizatorDTO();
                    dto.setUserId(utilizator.getUserId());
                    dto.setNume(utilizator.getNume());
                    dto.setEmail(utilizator.getEmail());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(utilizatori);
    }

    // Actualizare denumire echipă
    @PutMapping("/{echipaId}")
    public ResponseEntity<EchipaDTO> modificaEchipa(@PathVariable Integer echipaId, @Valid @RequestBody EchipaDTO echipaDTO) {
        workflowService.modificaEchipa(echipaId, echipaDTO.getDenumire());
        var echipa = workflowService.vizualizeazaEchipa(echipaId);
        var rezultatDTO = new EchipaDTO();
        rezultatDTO.setIdEchipa(echipa.getIdEchipa());
        rezultatDTO.setDenumire(echipa.getDenumire());
        return ResponseEntity.ok(rezultatDTO);
    }
}
