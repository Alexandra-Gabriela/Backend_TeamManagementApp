package org.Proiect.ServiciiRest;

import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Domain.App.TipUtilizator;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/utilizatori")
@Transactional
public class UtilizatorRESTService {

    private static final Logger logger = Logger.getLogger(UtilizatorRESTService.class.getName());

    @Autowired
    private AppUserRepository utilizatorRepository;

    @Autowired
    private ModelMapper modelMapper;

    // === GET: Toți utilizatorii ===
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UtilizatorDTO> getAllUtilizatori() {
        logger.info("Fetching all users...");
        return utilizatorRepository.findAll().stream()
                .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                .collect(Collectors.toList());
    }

    // === GET: Lideri ===
    @GetMapping(value = "/lideri", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UtilizatorDTO> getAllLideri() {
        logger.info("Fetching all leaders...");
        return utilizatorRepository.findAllByTipUtilizator(TipUtilizator.LIDER).stream()
                .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                .collect(Collectors.toList());
    }

    // === GET: Utilizator după ID ===
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilizatorDTO> getUtilizatorById(@PathVariable int id) {
        logger.info("Fetching user with ID: " + id);
        return utilizatorRepository.findById(id)
                .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // === GET: Membri ===
    @GetMapping(value = "/membri", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UtilizatorDTO> getAllMembri() {
        logger.info("Fetching all members...");
        return utilizatorRepository.findAllByTipUtilizator(TipUtilizator.MEMBRUECHIPA).stream()
                .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                .collect(Collectors.toList());
    }


    // === POST: Creare utilizator ===
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilizatorDTO> adaugaUtilizator(@RequestBody UtilizatorDTO utilizatorDTO) {
        logger.info("Adding a new user...");
        Utilizator utilizator = modelMapper.map(utilizatorDTO, Utilizator.class);
        utilizator = utilizatorRepository.save(utilizator);
        return ResponseEntity.ok(modelMapper.map(utilizator, UtilizatorDTO.class));
    }

    // === PUT: Actualizare utilizator ===
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UtilizatorDTO> actualizeazaUtilizator(@PathVariable int id, @RequestBody UtilizatorDTO utilizatorDTO) {
        logger.info("Updating user with ID: " + id);
        return utilizatorRepository.findById(id).map(existingUser -> {
            existingUser.setNume(utilizatorDTO.getNume());
            existingUser.setEmail(utilizatorDTO.getEmail());
            existingUser.setTipUtilizator(utilizatorDTO.getTipUtilizator());
            utilizatorRepository.save(existingUser);
            return ResponseEntity.ok(modelMapper.map(existingUser, UtilizatorDTO.class));
        }).orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Ștergere utilizator ===
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> stergeUtilizator(@PathVariable int id) {
        logger.info("Deleting user with ID: " + id);
        if (utilizatorRepository.existsById(id)) {
            utilizatorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    // === GET: Admini ===
    @GetMapping(value = "/admini", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UtilizatorDTO> getAllAdmini() {
        logger.info("Fetching all admins...");
        return utilizatorRepository.findAllByTipUtilizator(TipUtilizator.ADMIN).stream()
                .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                .collect(Collectors.toList());
    }

}
