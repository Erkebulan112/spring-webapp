package myrzakhan_taskflow.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "file.valid")
public class UploadFileValidationProperties {
    private long maxSize;
    private Set<String> allowedMimeTypes;
}
