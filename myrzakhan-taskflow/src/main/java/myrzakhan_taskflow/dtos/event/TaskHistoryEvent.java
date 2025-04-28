package myrzakhan_taskflow.dtos.event;

import lombok.Builder;
import java.util.Map;

@Builder
public record TaskHistoryEvent(
        Long taskId,
        String action,
        Long performedBy,
        Map<String, Object> details
) {
}
