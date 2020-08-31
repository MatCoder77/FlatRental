package com.flatrental.domain.locations.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrental.domain.locations.commune.Commune;
import com.flatrental.domain.locations.commune.CommuneService;
import com.flatrental.domain.locations.district.District;
import com.flatrental.domain.locations.district.DistrictService;
import com.flatrental.domain.locations.locality.Locality;
import com.flatrental.domain.locations.locality.LocalityService;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrict;
import com.flatrental.domain.locations.localitydistrict.LocalityDistrictService;
import com.flatrental.domain.locations.localitypart.LocalityPart;
import com.flatrental.domain.locations.localitypart.LocalityPartService;
import com.flatrental.domain.locations.street.Street;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flatrental.domain.locations.elasticsearch.MappingProvider.LOCATIONS_INDEX;
import static com.flatrental.domain.locations.elasticsearch.MappingProvider.SEARCH_FIELDS_WITH_BOOST;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final RestHighLevelClient elasticClient;
    private final MappingProvider mappingProvider;
    private final ObjectMapper objectMapper;
    private final VoivodeshipService voivodeshipService;
    private final DistrictService districtService;
    private final CommuneService communeService;
    private final LocalityService localityService;
    private final LocalityDistrictService localityDistrictService;
    private final LocalityPartService localityPartService;
    private final LocationSerachMapper locationSearchMapper;

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    public void createLocationIndex() throws IOException {
        elasticClient.indices().create(mappingProvider.getCreateRequestForLocationIndex(), RequestOptions.DEFAULT);
    }

    public void indexLocations() throws IOException {
        List<Voivodeship> voivodeships = voivodeshipService.getAllVoivodeships();
        List<District> districts = districtService.getAllDistricts();
        List<Commune> communes = communeService.getAllCommunes();
        List<Locality> autonomousLocalities = localityService.getAllLocalities();
        List<LocalityDistrict> localityDistricts = localityDistrictService.getAllLocalityDistricts();
        List<LocalityPart> localityParts = localityPartService.getAllLocalityParts();

        indexObjects(voivodeships, locationSearchMapper::mapToLocationSearchDTO);
        indexObjects(districts, locationSearchMapper::mapToLocationSearchDTO);
        indexObjects(communes, locationSearchMapper::mapToLocationSearchDTO);
        indexObjects(autonomousLocalities, locationSearchMapper::mapToLocationSearchDTO);
        indexObjects(localityDistricts, locationSearchMapper::mapToLocationSearchDTO);
        indexObjects(localityParts, locationSearchMapper::mapToLocationSearchDTO);
        indexStreets(autonomousLocalities, localityDistricts, localityParts);
    }

    private <T> void indexObjects(List<T> objectsToBeIndexed, Function<T, LocationSearchDTO> mapper) throws IOException {
        List<IndexRequest> indexRequests = objectsToBeIndexed.stream()
                .map(mapper)
                .map(this::mapToIndexRequest)
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest();
        indexRequests.forEach(bulkRequest::add);
        bulkRequest.timeout("5m");
        elasticClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    private IndexRequest mapToIndexRequest(Object object) {
        try {
            String jsonSource = objectMapper.writeValueAsString(object);
            IndexRequest indexRequest = new IndexRequest(LOCATIONS_INDEX);
            indexRequest.source(jsonSource, XContentType.JSON);
            return indexRequest;
        } catch (JsonProcessingException e) {
            logger.error("Error during json processing", e);
            throw new IllegalArgumentException("wrong");
        }
    }

    private void indexStreets(List<Locality> autonomousLocalities, List<LocalityDistrict> localityDistricts, List<LocalityPart> localityParts) throws IOException {
        Map<Locality, List<LocalityDistrict>> localityDistrictsByLocality = localityDistricts.stream()
                .collect(Collectors.groupingBy(LocalityDistrict::getParentLocality));
        Map<Locality, List<LocalityPart>> localityPartsByLocality = localityParts.stream()
                .collect(Collectors.groupingBy(LocalityPart::getParentLocality));
        List<LocationSearchDTO> locationSearchDTOS = autonomousLocalities.stream()
                .map(locality -> getLocationSearchDTOsWithStreetForLocality(locality, localityDistrictsByLocality, localityPartsByLocality))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        var partitionedLocationSearchDTOs = Lists.partition(locationSearchDTOS, 25000);
        for (var list : partitionedLocationSearchDTOs) {
            indexObjects(list, Function.identity());
        }
    }

    private List<LocationSearchDTO> getLocationSearchDTOsWithStreetForLocality(Locality locality,
                                                                               Map<Locality, List<LocalityDistrict>> localityDistrictsByLocality,
                                                                               Map<Locality, List<LocalityPart>> localityPartsByLocality) {
        List<LocalityDistrict> localityDistricts = localityDistrictsByLocality.getOrDefault(locality, Collections.emptyList());
        List<LocalityPart> localityParts = localityPartsByLocality.getOrDefault(locality, Collections.emptyList());
        Set<Street> processedStreets = new HashSet<>();
        List<LocationSearchDTO> localityPartLevelSearchDTOs = localityParts.stream()
                .map(localityPart -> mapToLocationSearchDTO(localityPart, processedStreets))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<LocationSearchDTO> localityDistrictLevelSearchDTOs = localityDistricts.stream()
                .map(localityDistrict -> mapToLocationSearchDTO(localityDistrict, processedStreets))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        processedStreets.addAll(localityDistricts.stream()
                .map(LocalityDistrict::getStreets)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        List<LocationSearchDTO> localityLevelSearchDTOs = mapToLocationSearchDTO(locality, processedStreets);
        return Stream.of(localityPartLevelSearchDTOs, localityDistrictLevelSearchDTOs, localityLevelSearchDTOs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<LocationSearchDTO> mapToLocationSearchDTO(LocalityPart localityPart, Set<Street> processedStreets) {
        processedStreets.addAll(localityPart.getStreets());
        return localityPart.getStreets().stream()
                .map(street -> locationSearchMapper.mapToLocationSearchDTO(localityPart, street))
                .collect(Collectors.toList());
    }

    private List<LocationSearchDTO> mapToLocationSearchDTO(LocalityDistrict localityDistrict, Set<Street> processedStreets) {
        return localityDistrict.getStreets().stream()
                .filter(street -> !processedStreets.contains(street))
                .map(street -> locationSearchMapper.mapToLocationSearchDTO(localityDistrict, street))
                .collect(Collectors.toList());
    }

    private List<LocationSearchDTO> mapToLocationSearchDTO(Locality locality, Set<Street> processedStreets) {
        return locality.getStreets().stream()
                .filter(street -> !processedStreets.contains(street))
                .map(street -> locationSearchMapper.mapToLocationSearchDTO(locality, street))
                .collect(Collectors.toList());
    }

    public List<LocationSearchDTO> searchLocation(String searchText) throws IOException {
        MultiMatchQueryBuilder multiMatchQueryBuilder = new MultiMatchQueryBuilder(searchText)
                .fields(SEARCH_FIELDS_WITH_BOOST)
                .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                .operator(Operator.AND);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(multiMatchQueryBuilder)
                .size(20);
        SearchRequest searchRequest = new SearchRequest(LOCATIONS_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = elasticClient.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);
    }

    private List<LocationSearchDTO> getSearchResult(SearchResponse response) {
        return Arrays.stream(response.getHits().getHits())
                .map(this::mapSearchHitToLocationSearchDTO)
                .collect(Collectors.toList());
    }

    private LocationSearchDTO mapSearchHitToLocationSearchDTO(SearchHit searchHit) {
        try {
            return objectMapper.readValue(searchHit.getSourceAsString(), LocationSearchDTO.class);
        } catch (IOException exception) {
            logger.error("Error", exception);
            throw new IllegalArgumentException("wrong");
        }
    }

    private void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(LOCATIONS_INDEX);
        elasticClient.indices().delete(request, RequestOptions.DEFAULT);
    }

    public void reindexLocations() throws IOException {
        if (indexExists()) {
            deleteIndex();
        }
        createLocationIndex();
        indexLocations();
    }

    private boolean indexExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest(LOCATIONS_INDEX);
        return elasticClient.indices().exists(request, RequestOptions.DEFAULT);
    }

}
