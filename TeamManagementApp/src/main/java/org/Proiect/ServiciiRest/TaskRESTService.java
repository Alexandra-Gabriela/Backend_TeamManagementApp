package org.Proiect.ServiciiRest;

import jakarta.annotation.PostConstruct;
import org.Proiect.DTO.TaskDTO;


import org.Proiect.Domain.Proiect.Task;
import org.Proiect.Servicii.Repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/servicii/taskuri")
@Transactional
public class TaskRESTService {

    private static final Logger logger = Logger.getLogger(TaskRESTService.class.getName());

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    // === GET: Toate task-urile ===
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        logger.info("Fetching all tasks...");
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> taskDTOs = tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
    }

    // === GET: Task după ID ===
    @GetMapping(value = "/{taskId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Integer taskId) {
        logger.info("Fetching task with ID: " + taskId);
        return taskRepository.findById(taskId)
                .map(task -> ResponseEntity.ok(modelMapper.map(task, TaskDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    // === POST: Creare task ===
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TaskDTO> creeazaTaskNou(@RequestBody TaskDTO taskDTO) {
        logger.info("Creating a new task: " + taskDTO);
        Task task = modelMapper.map(taskDTO, Task.class);
        Task savedTask = taskRepository.save(task);
        TaskDTO savedTaskDTO = modelMapper.map(savedTask, TaskDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTaskDTO);
    }

    // === PUT: Actualizare task ===
    @PutMapping(value = "/{taskId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TaskDTO> modificareTask(
            @PathVariable Integer taskId,
            @RequestBody TaskDTO taskDTO) {
        logger.info("Updating task with ID: " + taskId);
        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    Task updatedTask = modelMapper.map(taskDTO, Task.class);
                    updatedTask.setTaskUserId(existingTask.getTaskUserId());
                    Task savedTask = taskRepository.save(updatedTask);
                    TaskDTO savedTaskDTO = modelMapper.map(savedTask, TaskDTO.class);
                    return ResponseEntity.ok(savedTaskDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Ștergere task ===
    @DeleteMapping(value = "/{taskId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> stergereTask(@PathVariable Integer taskId) {
        logger.info("Deleting task with ID: " + taskId);
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === POST: Notificare lider pentru finalizarea taskului ===
    @PostMapping(value = "/{taskId}/notify-leader", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> trimiteNotificareLider(@PathVariable Integer taskId) {
        logger.info("Sending notification for task ID: " + taskId);
        // Logica notificării (de implementat)
        return ResponseEntity.ok().build();
    }
    @PostConstruct
    private void setUpMapper() {
        logger.info("Setting up ModelMapper configurations...");

        // Configurare mapare pentru Task -> TaskDTO
        TypeMap<Task, TaskDTO> taskDTOMapper = modelMapper.createTypeMap(Task.class, TaskDTO.class);
        taskDTOMapper.addMappings(mapper -> mapper.map(Task::getTaskUserId, TaskDTO::setIdTask));
        taskDTOMapper.addMappings(mapper -> mapper.map(Task::getDescriere, TaskDTO::setDescriere));
        taskDTOMapper.addMappings(mapper -> mapper.map(Task::getStatus, TaskDTO::setStatus));
        taskDTOMapper.addMappings(mapper -> mapper.map(Task::getDeadline, TaskDTO::setDeadline));

        // Configurare mapare pentru TaskDTO -> Task
        TypeMap<TaskDTO, Task> taskMapper = modelMapper.createTypeMap(TaskDTO.class, Task.class);
        taskMapper.addMappings(mapper -> mapper.map(TaskDTO::getIdTask, Task::setTaskUserId));
        taskMapper.addMappings(mapper -> mapper.map(TaskDTO::getDescriere, Task::setDescriere));
        taskMapper.addMappings(mapper -> mapper.map(TaskDTO::getStatus, Task::setStatus));
        taskMapper.addMappings(mapper -> mapper.map(TaskDTO::getDeadline, Task::setDeadline));

        // Convertor personalizat pentru LocalDate
        modelMapper.addConverter(ctx -> ctx.getSource() == null ? null : LocalDate.parse(ctx.getSource()),
                String.class, LocalDate.class);

        modelMapper.addConverter(ctx -> ctx.getSource() == null ? null : ctx.getSource().toString(),
                LocalDate.class, String.class);
    }


}
