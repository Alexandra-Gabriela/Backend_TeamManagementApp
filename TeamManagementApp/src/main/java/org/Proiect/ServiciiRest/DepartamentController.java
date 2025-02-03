package org.Proiect.ServiciiRest;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.Proiect.DTO.DepartamentDTO;
import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Servicii.IDepartamentWorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/departamente")
@Transactional
public class DepartamentController {

    private final IDepartamentWorkflowService workflowService;

    public DepartamentController(IDepartamentWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    // Creare departament
    @PostMapping
    public ResponseEntity<DepartamentDTO> creeazaDepartament(@Valid @RequestBody DepartamentDTO departamentDTO) {
        var departament = workflowService.creeazaDepartament(departamentDTO.getNumeDepartament(), departamentDTO.getManagerId());
        var rezultatDTO = new DepartamentDTO();
        rezultatDTO.setId(departament.getId());
        rezultatDTO.setNumeDepartament(departament.getNumeDepartament());
        rezultatDTO.setManagerId(departament.getManagerProiect().getEchipa().getIdEchipa());
        rezultatDTO.setManagerNume(departament.getManagerProiect().getNume());
        return ResponseEntity.ok(rezultatDTO);
    }

    // Adăugare utilizator în departament
    @PostMapping("/{departamentId}/utilizatori")
    public ResponseEntity<String> adaugaUtilizator(@PathVariable Integer departamentId, @RequestParam Integer userId, @RequestParam String rol) {
        workflowService.adaugaUtilizatorInDepartament(departamentId, userId, rol);
        return ResponseEntity.ok("Utilizator adăugat cu succes!");
    }

    // Vizualizare toate departamentele
    @GetMapping
    public ResponseEntity<List<DepartamentDTO>> getAllDepartamente() {
        var departamente = workflowService.getAllDepartamente() // Asumăm o metodă nouă în workflow service
                .stream()
                .map(departament -> {
                    var dto = new DepartamentDTO();
                    dto.setId(departament.getId());
                    dto.setNumeDepartament(departament.getNumeDepartament());
                    dto.setManagerId(departament.getManagerProiect().getUserId());
                    dto.setManagerNume(departament.getManagerProiect().getNume());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(departamente);
    }

    // Vizualizare utilizatori dintr-un departament
    @GetMapping("/{departamentId}/utilizatori")
    public ResponseEntity<List<UtilizatorDTO>> getMembriiDepartament(@PathVariable Integer departamentId) {
        var utilizatori = workflowService.vizualizeazaMembriiDepartament(departamentId)
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

    // Actualizare nume departament
    @PutMapping("/{departamentId}")
    public ResponseEntity<DepartamentDTO> modificaDepartament(@PathVariable Integer departamentId, @Valid @RequestBody DepartamentDTO departamentDTO) {
        workflowService.modificaDepartament(departamentId, departamentDTO.getNumeDepartament());
        var departament = workflowService.vizualizeazaDepartament(departamentId);
        var rezultatDTO = new DepartamentDTO();
        rezultatDTO.setId(departament.getId());
        rezultatDTO.setNumeDepartament(departament.getNumeDepartament());
        rezultatDTO.setManagerId(departament.getManagerProiect().getUserId());
        rezultatDTO.setManagerNume(departament.getManagerProiect().getNume());
        return ResponseEntity.ok(rezultatDTO);
    }
    public void adaugaMembruLaDepartament(Integer departamentId, UtilizatorDTO utilizator) {
        if (departamentId == null || utilizator == null) {
            throw new IllegalArgumentException("Departament ID și utilizatorul nu pot fi null");
        }
    }


}
