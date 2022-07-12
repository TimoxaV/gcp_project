//package gcp.project.cloud.repository.impl;
//
//import com.google.cloud.bigquery.BigQuery;
//import com.google.cloud.bigquery.FormatOptions;
//import com.google.cloud.bigquery.Job;
//import com.google.cloud.bigquery.JobInfo;
//import com.google.cloud.bigquery.JobStatistics;
//import com.google.cloud.bigquery.TableDataWriteChannel;
//import com.google.cloud.bigquery.TableId;
//import com.google.cloud.bigquery.WriteChannelConfiguration;
//import gcp.project.cloud.exceptions.DataProcessException;
//import gcp.project.cloud.exceptions.JobWaitingException;
//import gcp.project.cloud.repository.BigQueryRepository;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.nio.channels.Channels;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class BigQueryRepositoryImpl implements BigQueryRepository {
//    private final BigQuery bigQuery;
//
//    @Autowired
//    public BigQueryRepositoryImpl(BigQuery bigQuery) {
//        this.bigQuery = bigQuery;
//    }
//
//    @Override
//    public long writeToTable(String datasetName, String tableName, String jsonData) {
//        TableId tableId = TableId.of(datasetName, tableName);
//        WriteChannelConfiguration writeChannelConfiguration = WriteChannelConfiguration
//                .newBuilder(tableId)
//                .setFormatOptions(FormatOptions.json())
//                .setWriteDisposition(JobInfo.WriteDisposition.WRITE_APPEND)
//                .build();
//        TableDataWriteChannel writer = bigQuery.writer(writeChannelConfiguration);
//        try (OutputStream stream = Channels.newOutputStream(writer)) {
//            Files.copy(Path.of(jsonData), stream);
//        } catch (IOException e) {
//            throw new DataProcessException("Can't process json file", e);
//        }
//        Job job = writer.getJob();
//        try {
//            job = job.waitFor();
//        } catch (InterruptedException e) {
//            throw new JobWaitingException("Unable to execute job", e);
//        }
//        JobStatistics.LoadStatistics stats = job.getStatistics();
//        return stats.getOutputRows();
//    }
//}
