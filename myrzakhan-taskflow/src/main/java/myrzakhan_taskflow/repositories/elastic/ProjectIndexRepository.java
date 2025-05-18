package myrzakhan_taskflow.repositories.elastic;

import myrzakhan_taskflow.entities.elastic.ProjectIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectIndexRepository extends ElasticsearchRepository<ProjectIndex, Long> {
}
