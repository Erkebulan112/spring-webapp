package myrzakhan_taskflow.repositories.elastic;

import myrzakhan_taskflow.entities.elastic.CommentIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentIndexRepository extends ElasticsearchRepository<CommentIndex, Long> {
}
