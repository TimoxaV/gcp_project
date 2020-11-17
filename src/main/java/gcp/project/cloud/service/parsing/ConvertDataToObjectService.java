package gcp.project.cloud.service.parsing;

import java.util.List;

public interface ConvertDataToObjectService<T> {

    List<T> parseFileToObject();
}
