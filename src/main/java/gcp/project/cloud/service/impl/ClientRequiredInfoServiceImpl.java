//package gcp.project.cloud.service.impl;
//
//import gcp.project.cloud.model.Client;
//import gcp.project.cloud.model.ClientRequiredInfo;
//import gcp.project.cloud.repository.BigQueryRepository;
//import gcp.project.cloud.service.ClientRequiredInfoService;
//import gcp.project.cloud.service.parsing.ConvertObjectToDataService;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ClientRequiredInfoServiceImpl implements ClientRequiredInfoService {
//    private final ConvertObjectToDataService<ClientRequiredInfo> convertObjectToDataService;
//    private final BigQueryRepository bigQueryRepository;
//    private final ModelMapper modelMapper;
//
//    @Autowired
//    public ClientRequiredInfoServiceImpl(
//            ConvertObjectToDataService<ClientRequiredInfo> convertObjectToDataService,
//            BigQueryRepository bigQueryRepository,
//            ModelMapper modelMapper) {
//        this.convertObjectToDataService = convertObjectToDataService;
//        this.bigQueryRepository = bigQueryRepository;
//        this.modelMapper = modelMapper;
//    }
//
//    @Override
//    public List<ClientRequiredInfo> getClientRequiredDto(List<Client> clients) {
//        return clients.stream()
//                .map(c -> modelMapper.map(c, ClientRequiredInfo.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public void uploadClientsRequiredInfo(List<ClientRequiredInfo> clientRequiredInfos,
//                                          String dataSet,
//                                          String clientsRequiredTable,
//                                          String requiredJsonToUpload) {
//        convertObjectToDataService.writeObjectToFile(clientRequiredInfos, requiredJsonToUpload);
//        bigQueryRepository.writeToTable(dataSet, clientsRequiredTable, requiredJsonToUpload);
//    }
//}
