package gcp.project.cloud.service.parsing;

import java.util.List;

public interface ConvertObjectToDataService<T> {
    void writeObjectToFile(List<T> objects);
}
