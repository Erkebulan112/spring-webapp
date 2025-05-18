package myrzakhan_taskflow.repositories.elastic;

import myrzakhan_taskflow.entities.elastic.TaskIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskIndexRepository extends ElasticsearchRepository<TaskIndex, Long> {
}
