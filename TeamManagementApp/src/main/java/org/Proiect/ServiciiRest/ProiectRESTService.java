package org.Proiect.ServiciiRest;

import jakarta.annotation.PostConstruct;
import org.Proiect.DTO.ProiectDTO;
import org.Proiect.Domain.Proiect.Proiect;
import org.Proiect.Servicii.Repository.RepositoryProiect;
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
@RequestMapping("/rest/servicii/proiect") // REST.Resource Style
@Transactional
public class ProiectRESTService {

    private static final Logger logger = Logger.getLogger(ProiectRESTService.class.getName());
    @Autowired
    private RepositoryProiect proiectRepository;

    @Autowired
    private ModelMapper modelMapper;

    // === GET: Toate proiectele ===
    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Collection<ProiectDTO> getAll() {
        logger.info("Fetching all projects...");
        List<Proiect> proiecte = proiectRepository.findAll();
        return proiecte.stream()
                .map(proiect -> modelMapper.map(proiect, ProiectDTO.class))
                .collect(Collectors.toList());
    }

    // === GET: Proiect după ID ===
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ProiectDTO> getById(@PathVariable int id) {
        logger.info("Fetching project with ID: " + id);
        return proiectRepository.findById(id)
                .map(proiect -> modelMapper.map(proiect, ProiectDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST: Creare proiect ===
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ProiectDTO create(@RequestBody ProiectDTO proiectDTO) {
        logger.info("Creating a new project: " + proiectDTO);
        Proiect proiect = modelMapper.map(proiectDTO, Proiect.class);
        Proiect savedProiect = proiectRepository.save(proiect);
        return modelMapper.map(savedProiect, ProiectDTO.class);
    }

    // === PUT: Actualizare proiect ===
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ProiectDTO> update(@PathVariable int id, @RequestBody ProiectDTO proiectDTO) {
        logger.info("Updating project with ID: " + id);
        return proiectRepository.findById(id)
                .map(existing -> {
                    Proiect updatedProiect = modelMapper.map(proiectDTO, Proiect.class);
                    updatedProiect.setId(existing.getId());
                    Proiect savedProiect = proiectRepository.save(updatedProiect);
                    return ResponseEntity.ok(modelMapper.map(savedProiect, ProiectDTO.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Ștergere proiect ===
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable int id) {
        logger.info("Deleting project with ID: " + id);
        if (proiectRepository.existsById(id)) {
            proiectRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostConstruct
    private void setUpMapper() {
        logger.info("Setting up ModelMapper configurations...");
        TypeMap<Proiect, ProiectDTO> proiectDTOMapper = modelMapper.createTypeMap(Proiect.class, ProiectDTO.class);

        proiectDTOMapper.addMappings(mapper -> mapper.map(Proiect::getId, ProiectDTO::setId));
        proiectDTOMapper.addMappings(mapper -> mapper.map(Proiect::getDenumire, ProiectDTO::setDenumire));
        proiectDTOMapper.addMappings(mapper -> mapper.map(Proiect::getDataIncepere, ProiectDTO::setDataIncepere));
        proiectDTOMapper.addMappings(mapper -> mapper.map(Proiect::getDescriere, ProiectDTO::setDescriere));

        // Verifică dacă statusul este enum și convertește în String
        proiectDTOMapper.addMappings(mapper ->
                mapper.map(proiect -> proiect.getStatus() != null ? proiect.getStatus().toString() : null, ProiectDTO::setStatus)
        );
        proiectDTOMapper.addMappings(mapper -> mapper.map(Proiect::getDataFinalizare, ProiectDTO::setDataFinalizare));
    }

}
