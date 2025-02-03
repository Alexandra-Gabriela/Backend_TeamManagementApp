package org.Proiect.ServiciiRest;

import jakarta.annotation.PostConstruct;
import org.Proiect.DTO.DepartamentDTO;
import org.Proiect.DTO.UtilizatorDTO;
import org.Proiect.Domain.Angajati.Departament;
import org.Proiect.Servicii.Repository.DepartamentRepository;
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
@RequestMapping("/rest/servicii/departamente")
@Transactional
public class DepartamentRESTService {

    private static final Logger logger = Logger.getLogger(DepartamentRESTService.class.getName());

    @Autowired
    private DepartamentRepository departamentRepository;

    @Autowired
    private ModelMapper modelMapper;

    // === GET: Toate departamentele ===
    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Collection<DepartamentDTO> getAll() {
        logger.info("Fetching all departments...");
        List<Departament> departamente = departamentRepository.findAll();
        return departamente.stream()
                .map(departament -> modelMapper.map(departament, DepartamentDTO.class))
                .collect(Collectors.toList());
    }

    // === GET: Departament după ID ===
    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<DepartamentDTO> getById(@PathVariable int id) {
        logger.info("Fetching department with ID: " + id);
        return departamentRepository.findById(id)
                .map(departament -> modelMapper.map(departament, DepartamentDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST: Creare departament ===
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public DepartamentDTO create(@RequestBody DepartamentDTO departamentDTO) {
        logger.info("Creating a new department: " + departamentDTO);
        Departament departament = modelMapper.map(departamentDTO, Departament.class);
        Departament savedDepartament = departamentRepository.save(departament);
        return modelMapper.map(savedDepartament, DepartamentDTO.class);
    }

    // === PUT: Actualizare departament ===
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<DepartamentDTO> update(@PathVariable int id, @RequestBody DepartamentDTO departamentDTO) {
        logger.info("Updating department with ID: " + id);
        return departamentRepository.findById(id)
                .map(existing -> {
                    Departament updatedDepartament = modelMapper.map(departamentDTO, Departament.class);
                    updatedDepartament.setId(existing.getId());
                    Departament savedDepartament = departamentRepository.save(updatedDepartament);
                    return ResponseEntity.ok(modelMapper.map(savedDepartament, DepartamentDTO.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Ștergere departament ===
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable int id) {
        logger.info("Deleting department with ID: " + id);
        if (departamentRepository.existsById(id)) {
            departamentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostConstruct
    private void setUpMapper() {
        logger.info("Setting up ModelMapper configurations...");
        TypeMap<Departament, DepartamentDTO> departamentDTOMapper = modelMapper.createTypeMap(Departament.class, DepartamentDTO.class);

        departamentDTOMapper.addMappings(mapper -> mapper.map(Departament::getId, DepartamentDTO::setId));
        departamentDTOMapper.addMappings(mapper -> mapper.map(Departament::getNumeDepartament, DepartamentDTO::setNumeDepartament));

    }
    // === GET: Membrii unui departament ===
    @RequestMapping(value = "/{departamentId}/membri", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<List<UtilizatorDTO>> getMembriiDepartament(@PathVariable int departamentId) {
        logger.info("Fetching members for department with ID: " + departamentId);
        List<UtilizatorDTO> membri = departamentRepository.findById(departamentId)
                .map(departament -> departament.getAngajati().stream()
                        .map(utilizator -> modelMapper.map(utilizator, UtilizatorDTO.class))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Departamentul nu există"));

        return ResponseEntity.ok(membri);
    }


}
