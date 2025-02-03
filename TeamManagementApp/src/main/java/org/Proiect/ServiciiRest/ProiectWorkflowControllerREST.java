package org.Proiect.ServiciiRest;

import org.Proiect.DTO.ProiectDTO;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Servicii.IProiecteWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/proiecteWorkflow")
public class ProiectWorkflowControllerREST {

    @Autowired
    private IProiecteWorkflowService proiectWorkflowService;

    @PostMapping
    public ResponseEntity<ProiectDTO> creareProiect(@RequestBody ProiectDTO proiectDTO) {
        Proiect proiect = new Proiect();
        proiect.setDenumire(proiectDTO.getDenumire());
        proiect.setDescriere(proiectDTO.getDescriere());
        proiect.setDataIncepere(proiectDTO.getDataIncepere());

        Proiect proiectCreat = proiectWorkflowService.creareProiect(
                proiectDTO.getDenumire(),
                proiectDTO.getDescriere(),
                proiect.getLider(),
                proiect.getEchipe(),
                proiectDTO.getDataIncepere()
        );

        ProiectDTO proiectDTOResponse = new ProiectDTO();
        proiectDTOResponse.setId(proiectCreat.getId());
        proiectDTOResponse.setDenumire(proiectCreat.getDenumire());
        proiectDTOResponse.setDescriere(proiectCreat.getDescriere());
        proiectDTOResponse.setStatus(proiectCreat.getStatus().name());
        proiectDTOResponse.setDataIncepere(proiectCreat.getDataIncepere());
        proiectDTOResponse.setDataFinalizare(proiectCreat.getDataFinalizare());

        return new ResponseEntity<>(proiectDTOResponse, HttpStatus.CREATED);
    }

    // Endpoint pentru obținerea unui proiect
    @GetMapping("/{proiectId}")
    public ResponseEntity<ProiectDTO> getProiectById(@PathVariable Integer proiectId) {
        Proiect proiect = proiectWorkflowService.getProiectById(proiectId);

        ProiectDTO proiectDTO = new ProiectDTO();
        proiectDTO.setId(proiect.getId());
        proiectDTO.setDenumire(proiect.getDenumire());
        proiectDTO.setDescriere(proiect.getDescriere());
        proiectDTO.setStatus(proiect.getStatus().name());
        proiectDTO.setDataIncepere(proiect.getDataIncepere());
        proiectDTO.setDataFinalizare(proiect.getDataFinalizare());

        return new ResponseEntity<>(proiectDTO, HttpStatus.OK);
    }

    // Endpoint pentru obținerea tuturor proiectelor
    @GetMapping
    public ResponseEntity<List<ProiectDTO>> getToateProiectele() {
        List<Proiect> proiecte = proiectWorkflowService.getToateProiectele();

        List<ProiectDTO> proiecteDTO = proiecte.stream().map(proiect -> {
            ProiectDTO proiectDTO = new ProiectDTO();
            proiectDTO.setId(proiect.getId());
            proiectDTO.setDenumire(proiect.getDenumire());
            proiectDTO.setDescriere(proiect.getDescriere());
            proiectDTO.setStatus(proiect.getStatus().name());
            proiectDTO.setDataIncepere(proiect.getDataIncepere());
            proiectDTO.setDataFinalizare(proiect.getDataFinalizare());
            return proiectDTO;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(proiecteDTO, HttpStatus.OK);
    }

    // Endpoint pentru modificarea unui proiect
    @PutMapping("/{proiectId}")
    public ResponseEntity<ProiectDTO> modificaProiect(@PathVariable Integer proiectId, @RequestBody ProiectDTO proiectDTO) {
        Proiect proiect = proiectWorkflowService.getProiectById(proiectId);
        proiect.setDenumire(proiectDTO.getDenumire());
        proiect.setDescriere(proiectDTO.getDescriere());
        proiect.setDataIncepere(proiectDTO.getDataIncepere());

        Proiect proiectModificat = proiectWorkflowService.creareProiect(
                proiectDTO.getDenumire(),
                proiectDTO.getDescriere(),
                proiect.getLider(),
                proiect.getEchipe(),
                proiectDTO.getDataIncepere()
        );

        ProiectDTO proiectDTOResponse = new ProiectDTO();
        proiectDTOResponse.setId(proiectModificat.getId());
        proiectDTOResponse.setDenumire(proiectModificat.getDenumire());
        proiectDTOResponse.setDescriere(proiectModificat.getDescriere());
        proiectDTOResponse.setStatus(proiectModificat.getStatus().name());
        proiectDTOResponse.setDataIncepere(proiectModificat.getDataIncepere());
        proiectDTOResponse.setDataFinalizare(proiectModificat.getDataFinalizare());

        return new ResponseEntity<>(proiectDTOResponse, HttpStatus.OK);
    }

    // Endpoint pentru ștergerea unui proiect
    @DeleteMapping("/{proiectId}")
    public ResponseEntity<Void> stergeProiect(@PathVariable Integer proiectId) {
        boolean stergereSucces = proiectWorkflowService.stergeProiect(proiectId);

        if (stergereSucces) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}