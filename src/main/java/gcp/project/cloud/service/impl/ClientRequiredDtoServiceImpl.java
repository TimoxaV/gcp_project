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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientRequiredDtoServiceImpl implements ClientRequiredDtoService {
    private final ConvertObjectToDataService<ClientRequiredDto> convertObjectToDataService;
    private final BigQueryRepository bigQueryRepository;
    private final ModelMapper modelMapper;
    @Value("${data.set}")
    private String dataSet;
    @Value("${clients.required.table}")
    private String clientsRequiredTable;
    @Value("${json.to.upload.required.fields}")
    private String requiredJsonToUpload;

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
    public void uploadClientsRequiredDto(List<ClientRequiredDto> clientRequiredDtos) {
        convertObjectToDataService.writeObjectToFile(clientRequiredDtos);
        bigQueryRepository.writeToTable(dataSet, clientsRequiredTable, requiredJsonToUpload);
    }
}
