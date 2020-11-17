package gcp.project.cloud.repository;

public interface BigQueryRepository {
    long writeToTable(String datasetName, String tableName, String jsonData);
}
