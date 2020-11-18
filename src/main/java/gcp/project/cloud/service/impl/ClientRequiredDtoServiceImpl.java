package gcp.project.cloud.service.impl;

import gcp.project.cloud.model.Client;
import gcp.project.cloud.model.ClientRequiredDto;
import gcp.project.cloud.repository.BigQueryRepository;
import gcp.project.cloud.service.ClientRequiredDtoService;
import gcp.project.cloud.service.parsing.ConvertObjectToDataService;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientRequiredDtoServiceImpl implements ClientRequiredDtoService {
    private final ConvertObjectToDataService<ClientRequiredDto> convertObjectToDataService;
    private final BigQueryRepository bigQueryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientRequiredDtoServiceImpl(
            ConvertObjectToDataService<ClientRequiredDto> convertObjectToDataService,
            BigQueryRepository bigQueryRepository,
            ModelMapper modelMapper) {
        this.convertObjectToDataService = convertObjectToDataService;
        this.bigQueryRepository = bigQueryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ClientRequiredDto> getClientRequiredDto(List<Client> clients) {
        return clients.stream()
                .map(c -> modelMapper.map(c, ClientRequiredDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void uploadClientsRequiredDto(List<ClientRequiredDto> clientRequiredDtos, String dataSet,
                                         String clientsRequiredTable, String requiredJsonToUpload) {
        convertObjectToDataService.writeObjectToFile(clientRequiredDtos, requiredJsonToUpload);
        bigQueryRepository.writeToTable(dataSet, clientsRequiredTable, requiredJsonToUpload);
    }
}
