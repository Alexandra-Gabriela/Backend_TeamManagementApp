package org.Proiect.ServiciiRest;


import org.Proiect.DTO.BadgeDTO;
import org.Proiect.DTO.CursDTO;
import org.Proiect.DTO.UtilizatorCursDTO;
import org.Proiect.Domain.Angajati.Utilizator;
import org.Proiect.Domain.Dezvoltare.Badge;
import org.Proiect.Domain.Dezvoltare.Curs;
import org.Proiect.Domain.Dezvoltare.UtilizatorCurs;
import org.Proiect.Servicii.IDezvoltareWorkflowService;
import org.Proiect.Servicii.Repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/dezvoltare")
public class DezvoltareController {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private IDezvoltareWorkflowService dezvoltareService;

    @RequestMapping(value = "/cursuri", method = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CursDTO> creeazaCurs(@RequestBody CursDTO cursDTO) {
        // Obținem ID-ul utilizatorului din DTO (Admin)
        Integer adminId = cursDTO.getAdminId().getUserId();

        // Căutăm utilizatorul din baza de date pe baza ID-ului (Admin)
        Utilizator admin = appUserRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Utilizatorul cu ID-ul " + adminId + " nu a fost găsit"));

        // Creăm cursul folosind entitatea Utilizator
        Curs curs = dezvoltareService.creeazaCurs(
                cursDTO.getTitlu(),
                admin,  // Transmitem obiectul Utilizator la serviciu
                cursDTO.getDescriere(),
                cursDTO.getDurataOre());

        // Returnăm un DTO cu datele cursului creat
        return ResponseEntity.ok(curs.toDTO());
    }




    // Obținere detalii curs
    @RequestMapping(value = "/cursuri/{id}", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CursDTO> vizualizeazaCurs(@PathVariable Integer id, @RequestParam Integer utilizatorId) {
        return dezvoltareService.vizualizeazaCurs(id, utilizatorId)
                .map(curs -> ResponseEntity.ok(curs.toDTO()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Editare curs
    @RequestMapping(value = "/cursuri/{id}", method = RequestMethod.PUT,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CursDTO> editeazaCurs(@PathVariable Integer id, @RequestBody CursDTO cursDTO) {
        Curs curs = dezvoltareService.editeazaCurs(id, cursDTO.getTitlu());
        return ResponseEntity.ok(curs.toDTO());
    }

    // Ștergere curs
    @RequestMapping(value = "/cursuri/{id}", method = RequestMethod.DELETE,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> stergeCurs(@PathVariable Integer id) {
        dezvoltareService.stergeCurs(id);
        return ResponseEntity.noContent().build();
    }

    // Obținere toate cursurile
    @RequestMapping(value = "/cursuri", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<CursDTO>> obtineToateCursurile() {
        List<CursDTO> cursuri = dezvoltareService.obtineToateCursurile().stream()
                .map(Curs::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cursuri);
    }

    // Asignarea utilizatorilor la curs
    @RequestMapping(value = "/cursuri/{id}/utilizatori", method = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> asigneazaUtilizatoriLaCurs(@PathVariable Integer id, @RequestBody List<Integer> utilizatorIds) {
        dezvoltareService.asigneazaUtilizatoriLaCurs(id, utilizatorIds);
        return ResponseEntity.ok().build();
    }

    // Obținere toate badge-urile
    @RequestMapping(value = "/badges", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<BadgeDTO>> obtineToateBadgeurile() {
        List<BadgeDTO> badges = dezvoltareService.getAllBadges().stream()
                .map(Badge::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(badges);
    }

    // Generare badge pentru utilizator
    @RequestMapping(value = "/badges/{cursId}/utilizator/{utilizatorId}", method = RequestMethod.POST,
            consumes = { MediaType.APPLICATION_JSON_VALUE},
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BadgeDTO> genereazaBadge(@PathVariable Integer cursId, @PathVariable Integer utilizatorId) {
        Badge badge = dezvoltareService.genereazaBadgePentruCurs(cursId, utilizatorId);
        return ResponseEntity.ok(badge.toDTO());
    }

    // Urmărire progres utilizatori într-un curs
    @RequestMapping(value = "/cursuri/{cursId}/progres", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UtilizatorCursDTO>> urmaresteProgres(@PathVariable Integer cursId, @RequestParam Integer managerId) {
        List<UtilizatorCursDTO> progres = dezvoltareService.urmaresteProgresAngajati(cursId, managerId).stream()
                .map(UtilizatorCurs::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(progres);
    }

    // Urmărire progres propriu într-un curs
    @RequestMapping(value = "/cursuri/{cursId}/utilizator/{utilizatorId}/progres", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UtilizatorCursDTO> urmaresteProgresPropriu(@PathVariable Integer cursId, @PathVariable Integer utilizatorId) {
        UtilizatorCurs progres = dezvoltareService.urmaresteProgresPropriu(cursId, utilizatorId);
        return ResponseEntity.ok(progres.toDTO());
    }

    // Obținere detalii utilizatori asignați unui curs (progres, completare etc.)
    @RequestMapping(value = "/cursuri/{cursId}/utilizatori/detalii", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<UtilizatorCursDTO>> obtineDetaliiUtilizatori(@PathVariable Integer cursId) {
        // Apelează serviciul pentru a obține utilizatorii și progresul lor
        List<UtilizatorCursDTO> detaliiUtilizatori = dezvoltareService.obtineDetaliiUtilizatoriCurs(cursId);
        return ResponseEntity.ok(detaliiUtilizatori);
    }

}
