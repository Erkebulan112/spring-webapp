package myrzakhan_taskflow.config;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file.valid")
public class UploadFileValidationProperties {
    private long maxSize;
    private Set<String> allowedMimeTypes;
}
