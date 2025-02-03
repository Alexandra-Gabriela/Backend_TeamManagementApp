package org.Proiect.ServiciiRest;

import jakarta.annotation.PostConstruct;
import org.Proiect.DTO.EchipaDTO;
import org.Proiect.Domain.Angajati.Echipa;
import org.Proiect.Servicii.Repository.EchipaRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/echipe")
@Transactional
public class EchipaRESTService {

    private static final Logger logger = Logger.getLogger(EchipaRESTService.class.getName());

    @Autowired
    private EchipaRepository echipaRepository;

    @Autowired
    private ModelMapper modelMapper;

    // === GET: Toate echipele ===
    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Collection<EchipaDTO> getAll() {
        logger.info("Fetching all teams...");
        List<Echipa> echipe = echipaRepository.findAll();
        return echipe.stream()
                .map(echipa -> modelMapper.map(echipa, EchipaDTO.class))
                .collect(Collectors.toList());
    }

    // === GET: Echipa după ID ===
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<EchipaDTO> getById(@PathVariable int id) {
        logger.info("Fetching team with ID: " + id);
        return echipaRepository.findById(id)
                .map(echipa -> modelMapper.map(echipa, EchipaDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST: Creare echipă ===
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public EchipaDTO create(@RequestBody EchipaDTO echipaDTO) {
        logger.info("Creating a new team: " + echipaDTO);
        Echipa echipa = modelMapper.map(echipaDTO, Echipa.class);
        Echipa savedEchipa = echipaRepository.save(echipa);
        return modelMapper.map(savedEchipa, EchipaDTO.class);
    }

    // === PUT: Actualizare echipă ===
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<EchipaDTO> update(@PathVariable int id, @RequestBody EchipaDTO echipaDTO) {
        logger.info("Updating team with ID: " + id);
        return echipaRepository.findById(id)
                .map(existing -> {
                    Echipa updatedEchipa = modelMapper.map(echipaDTO, Echipa.class);
                    updatedEchipa.setIdEchipa(existing.getIdEchipa());
                    Echipa savedEchipa = echipaRepository.save(updatedEchipa);
                    return ResponseEntity.ok(modelMapper.map(savedEchipa, EchipaDTO.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Ștergere echipă ===
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable int id) {
        logger.info("Deleting team with ID: " + id);
        if (echipaRepository.existsById(id)) {
            echipaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostConstruct
    private void setUpMapper() {
        logger.info("Setting up ModelMapper configurations...");
        TypeMap<Echipa, EchipaDTO> echipaDTOMapper = modelMapper.createTypeMap(Echipa.class, EchipaDTO.class);

        echipaDTOMapper.addMappings(mapper -> mapper.map(Echipa::getIdEchipa, EchipaDTO::setIdEchipa));
        echipaDTOMapper.addMappings(mapper -> mapper.map(Echipa::getDenumire, EchipaDTO::setDenumire));
    }
}
