package myrzakhan_taskflow.services.postgres.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myrzakhan_taskflow.cache.pub.RedisEventPublisher;
import myrzakhan_taskflow.dtos.event.CacheEvent;
import myrzakhan_taskflow.dtos.event.ProjectIndexDelete;
import myrzakhan_taskflow.dtos.event.ProjectIndexEvent;
import myrzakhan_taskflow.dtos.requests.ProjectCreateRequest;
import myrzakhan_taskflow.dtos.requests.ProjectUpdateRequest;
import myrzakhan_taskflow.entities.elastic.ProjectIndex;
import myrzakhan_taskflow.entities.postgres.Project;
import myrzakhan_taskflow.exceptions.NotFoundException;
import myrzakhan_taskflow.message.KafkaLogProducer;
import myrzakhan_taskflow.repositories.postgres.ProjectRepository;
import myrzakhan_taskflow.services.postgres.ProjectService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final KafkaLogProducer kafkaLogProducer;
    private final ElasticsearchOperations elasticsearchOperations;
    private final RedisEventPublisher redisEventPublisher;

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAllProjects(Pageable pageable) {
        log.info("Get all projects");
        return projectRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = "projects", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public Project findProjectById(Long id) {
        log.info("Get project by id: {}", id);
        return projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: %d".formatted(id)));
    }

    @Override
    @CachePut(value = "projects", key = "#result.id")
    public Project createProject(ProjectCreateRequest request) {
        log.info("Create project: {}", request);
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        projectRepository.save(project);

        kafkaLogProducer.sendIndexEvent(ProjectIndexEvent.toDto(project));
        redisEventPublisher.publishCacheEvent(CacheEvent.createEvent("users", project.getId(), project));
        return project;
    }

    @Override
    @CachePut(value = "projects", key = "#id")
    public Project updateProject(Long id, ProjectUpdateRequest request) {
        log.info("Update project: {}", request);
        var project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project not found with id: %d".formatted(id)));
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(request.status());
        projectRepository.save(project);

        kafkaLogProducer.sendIndexEvent(ProjectIndexEvent.toDto(project));
        redisEventPublisher.publishCacheEvent(CacheEvent.updateEvent("projects", project.getId(), project));
        return project;
    }

    @Override
    @CacheEvict(value = "projects", key = "#id")
    public void deleteProject(Long id) {
        log.info("Delete project: {}", id);
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with id: %d".formatted(id)));
        projectRepository.deleteById(id);

        kafkaLogProducer.sendIndexEvent(new ProjectIndexDelete(project.getId()));
        redisEventPublisher.publishCacheEvent(CacheEvent.evictEvent("users", project.getId()));
    }

    @Override
    public List<ProjectIndex> searchProjects(String query) {
        log.info("Search projects: {}", query);

        Criteria criteria = new Criteria("name").matches(query)
                .or(new Criteria("description").matches(query));

        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        SearchHits<ProjectIndex> searchHits = elasticsearchOperations.search(criteriaQuery, ProjectIndex.class);

        return searchHits.stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
