package myrzakhan_taskflow.services.postgres;

import myrzakhan_taskflow.controllers.PageableConstants;
import myrzakhan_taskflow.dtos.event.TaskHistoryEvent;
import myrzakhan_taskflow.dtos.requests.TaskCreateRequest;
import myrzakhan_taskflow.dtos.requests.TaskUpdateRequest;
import myrzakhan_taskflow.entities.enums.Priority;
import myrzakhan_taskflow.entities.enums.TaskStatus;
import myrzakhan_taskflow.entities.postgres.Task;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.message.TaskHistoryEventProducer;
import myrzakhan_taskflow.repositories.postgres.TaskRepository;
import myrzakhan_taskflow.services.postgres.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryEventProducer producer;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private Task task1;
    private Task task2;
    private Task task3;
    private TaskCreateRequest taskCreateRequest;
    private TaskUpdateRequest taskUpdateRequest;
    private Page<Task> page;

    @BeforeEach
    void setUp() {

        taskCreateRequest = mock(TaskCreateRequest.class);
        taskUpdateRequest = mock(TaskUpdateRequest.class);

        task = new Task();
        task.setTitle(taskCreateRequest.title());
        task.setDescription(taskCreateRequest.description());
        task.setStatus(taskCreateRequest.status());
        task.setPriority(taskCreateRequest.priority());
        task.setDeadline(taskCreateRequest.deadline());
        task.setCreatedAt(LocalDateTime.now());

        task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus(TaskStatus.TODO);
        task1.setPriority(Priority.MEDIUM);

        task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus(TaskStatus.DONE);
        task2.setPriority(Priority.HIGH);

        task3 = new Task();
        task3.setTitle("Task 3");
        task3.setDescription("Description 3");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3.setPriority(Priority.LOW);

        taskCreateRequest = TaskCreateRequest.builder()
                .title("Title")
                .description("Description")
                .status(TaskStatus.TODO)
                .priority(Priority.HIGH)
                .deadline(LocalDateTime.now().plusDays(7))
                .build();

        taskUpdateRequest = TaskUpdateRequest.builder()
                .title("Title")
                .description("Description")
                .status(TaskStatus.TODO)
                .priority(Priority.HIGH)
                .deadline(LocalDateTime.now().plusDays(7))
                .build();

        page = new PageImpl<>(Arrays.asList(task1, task2, task3));
    }

    @Test
    void testFindAllTasks() {
        var pageable = PageRequest.of(PageableConstants.DEFAULT_PAGE,
                PageableConstants.DEFAULT_SIZE,
                Sort.by(Sort.Direction.DESC, PageableConstants.DEFAULT_SORT_BY));

        when(taskRepository.findAll(pageable)).thenReturn(page);
        var responses = taskService.findAllTasks(pageable);

        assertAll("Testing method findAllTasks",
                () -> assertNotNull(responses, "Response is null"),
                () -> assertEquals(3, responses.getContent().size(), "Wrong number of items"),
                () -> assertEquals("Task 1", responses.getContent().getFirst().getTitle(), "Wrong title"),
                () -> verify(taskRepository, times(1)).findAll(pageable));
    }

    @Test
    void testFindTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        var response = taskService.findTaskById(1L);

        assertAll("Testing findTaskById method",
                () -> assertNotNull(response, "Response is null"),
                () -> assertEquals(task.getTitle(), response.getTitle(), "Title does not match"),
                () -> assertEquals(task, response, "Objects do not match"),
                () -> verify(taskRepository, times(1)).findById(1L));
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        var response = taskService.createTask(taskCreateRequest);

        assertAll("Testing method createTask",
                () -> assertNotNull(response, "Created task must not be null"),
                () -> assertEquals(task.getTitle(), response.getTitle(), "Title does not match"),
                () -> assertEquals(task.getDescription(), response.getDescription(), "Description does not match"),
                () -> verify(taskRepository, times(1)).save(any(Task.class)));

        var routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var eventCaptor = ArgumentCaptor.forClass(TaskHistoryEvent.class);

        verify(producer, times(1)).sendTaskHistoryEvent(
                eq("task.history.exchange"),
                routingKeyCaptor.capture(),
                eventCaptor.capture()
        );
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        var response = taskService.updateTask(1L, taskUpdateRequest);

        assertAll("Testing updateTask method",
                () -> assertNotNull(response, "Updated task must not be null"),
                () -> assertEquals(task.getTitle(), response.getTitle(), "Title does not match"),
                () -> verify(taskRepository, times(1)).save(any(Task.class)));

        var routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        var eventCaptor = ArgumentCaptor.forClass(TaskHistoryEvent.class);

        verify(producer, times(1)).sendTaskHistoryEvent(
                eq("task.history.exchange"),
                routingKeyCaptor.capture(),
                eventCaptor.capture()
        );
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> taskService.findTaskById(999L), "Expected NotFoundException");
    }

    @Test
    void testValidationCheck_emptyTitle() {
        taskCreateRequest.builder().title("").build();
        task.setTitle("");
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        var result = taskService.createTask(taskCreateRequest);
        assertThat(result.getTitle()).isEmpty();
    }
}
