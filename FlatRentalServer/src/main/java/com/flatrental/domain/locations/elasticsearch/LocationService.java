package com.flatrental.domain.locations.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flatrental.domain.locations.abstractlocality.AbstractLocalityService;
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
import com.flatrental.domain.locations.street.StreetService;
import com.flatrental.domain.locations.voivodeship.Voivodeship;
import com.flatrental.domain.locations.voivodeship.VoivodeshipService;
import com.google.common.collect.Lists;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LocationService {

    @Autowired
    private RestHighLevelClient elasticClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VoivodeshipService voivodeshipService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private LocalityDistrictService localityDistrictService;

    @Autowired
    private LocalityPartService localityPartService;

    @Autowired
    private StreetService streetService;

    @Autowired
    private LocationSerachMapper locationSearchMapper;

    @Autowired
    private AbstractLocalityService abstractLocalityService;


    private static final String LOCATIONS_INDEX = "locations";
    private static final String ANALYSIS = "analysis";
    private static final String FILTER = "filter";
    private static final String EDGE_NGRAM_FILTER = "edgeNGramFilter";
    private static final String TYPE_PROPERTY = "type";
    private static final String MIN_GRAM_LENGTH_PROPERTY = "min_gram";
    private static final String MAX_GRAM_LENGTH_PROPERTY = "max_gram";
    private static final String SIDE_PROPERTY = "side";
    private static final String TOKENIZER = "tokenizer";
    private static final String LOWERCASE_FILTER = "lowercase";
    private static final String ASCII_FOLDING_FILTER = "asciifolding";
    private static final String INDEX_AUTOCOMPLETE_ANALYZER = "index_autocomplete_analyzer";
    private static final String SEARCH_AUTOCOMPLETE_ANALYZER = "search_autocomplete_analyzer";
    private static final String ANALYZER = "analyzer";
    private static final String SEARCH_ANALYZER = "search_analyzer";
    private static final String DYNAMIC_PROPERTY = "dynamic";
    private static final String PROPERTIES = "properties";
    private static final String INDEX_PROPERTY = "index";

    private static final Map<String, Float> searchFieldsWithBoost = Map.of(
            "voivodeship.name", 1.0f,
            "district.name", 1.0f,
            "commune.name", 1.0f,
            "locality.name", 8.0f,
            "localityDistrict.name", 1.0f,
            "localityPart.name", 1.0f,
            "street.main_name", 1.0f,
            "street.leading_name", 1.0f);

    public void createLocationIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(LOCATIONS_INDEX);
        createIndexRequest.settings(createLocationsIndexSettings());
        createIndexRequest.mapping(createLocationsIndexMapping());
        CreateIndexResponse createIndexResponse = elasticClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    private Map<String, Object> createLocationsIndexSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put(ANALYSIS, createAnalysis());
        return settings;
    }

    private Map<String, Object> createAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        analysis.put(FILTER, createFilters());
        analysis.put(ANALYZER, createAnalyzers());
        return analysis;
    }

    private Map<String, Object> createFilters() {
        Map<String, Object> filters = new HashMap<>();
        filters.put(EDGE_NGRAM_FILTER, createEdgeNGramFilter());
        return filters;
    }

    private Map<String, Object> createEdgeNGramFilter() {
        Map<String, Object> edgeNGramFilterProperties = new HashMap<>();
        edgeNGramFilterProperties.put(TYPE_PROPERTY, "edgeNGram");
        edgeNGramFilterProperties.put(MIN_GRAM_LENGTH_PROPERTY, 1);
        edgeNGramFilterProperties.put(MAX_GRAM_LENGTH_PROPERTY, 30);
        edgeNGramFilterProperties.put(SIDE_PROPERTY, "front");
        return edgeNGramFilterProperties;
    }

    private Map<String, Object> createAnalyzers() {
        Map<String, Object> analyzers = new HashMap<>();
        analyzers.put(INDEX_AUTOCOMPLETE_ANALYZER, createIndexAutocompleteAnalyzer());
        analyzers.put(SEARCH_AUTOCOMPLETE_ANALYZER, createSearchAutocompleteAnalyzer());
        return analyzers;
    }

    private Map<String, Object> createIndexAutocompleteAnalyzer() {
        Map<String, Object> indexAutocompleteAnalyzerProperties = new HashMap<>();
        indexAutocompleteAnalyzerProperties.put(TYPE_PROPERTY, "custom");
        indexAutocompleteAnalyzerProperties.put(TOKENIZER, "standard");
        indexAutocompleteAnalyzerProperties.put(FILTER, List.of(LOWERCASE_FILTER, ASCII_FOLDING_FILTER, EDGE_NGRAM_FILTER));
        return indexAutocompleteAnalyzerProperties;
    }

    private Map<String, Object> createSearchAutocompleteAnalyzer() {
        Map<String, Object> searchAutocompleteAnalyzerProperties = new HashMap<>();
        searchAutocompleteAnalyzerProperties.put(TYPE_PROPERTY, "custom");
        searchAutocompleteAnalyzerProperties.put(TOKENIZER, "standard");
        searchAutocompleteAnalyzerProperties.put(FILTER, List.of(LOWERCASE_FILTER, ASCII_FOLDING_FILTER));
        return searchAutocompleteAnalyzerProperties;
    }

    private Map<String, Object> createLocationsIndexMapping() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put(DYNAMIC_PROPERTY, "strict");
        mapping.put(PROPERTIES, createMappingProperties());
        return mapping;
    }

    private Map<String, Object> createMappingProperties() {
        Map<String, Object> mappingProperties = new HashMap<>();

        Map<String, Object> idFieldProperties = new HashMap<>();
        idFieldProperties.put(TYPE_PROPERTY, FieldType.LONG.getType());
        idFieldProperties.put(INDEX_PROPERTY, false);

        Map<String, Object> typeFieldProperties = new HashMap<>();
        typeFieldProperties.put(TYPE_PROPERTY, FieldType.KEYWORD.getType());
        typeFieldProperties.put(INDEX_PROPERTY, false);

        Map<String, Object> nameFieldProperties = new HashMap<>();
        nameFieldProperties.put(TYPE_PROPERTY, FieldType.TEXT.getType());
        nameFieldProperties.put(ANALYZER, INDEX_AUTOCOMPLETE_ANALYZER);
        nameFieldProperties.put(SEARCH_ANALYZER, SEARCH_AUTOCOMPLETE_ANALYZER);

        mappingProperties.put("voivodeship.id", idFieldProperties);
        mappingProperties.put("voivodeship.name", nameFieldProperties);

        mappingProperties.put("district.id", idFieldProperties);
        mappingProperties.put("district.name", nameFieldProperties);
        mappingProperties.put("district.type", typeFieldProperties);

        mappingProperties.put("commune.id", idFieldProperties);
        mappingProperties.put("commune.name", nameFieldProperties);
        mappingProperties.put("commune.type", typeFieldProperties);

        mappingProperties.put("locality.id", idFieldProperties);
        mappingProperties.put("locality.name", nameFieldProperties);
        mappingProperties.put("locality.type", typeFieldProperties);

        mappingProperties.put("localityDistrict.id", idFieldProperties);
        mappingProperties.put("localityDistrict.name", nameFieldProperties);
        mappingProperties.put("localityDistrict.type", typeFieldProperties);

        mappingProperties.put("localityPart.id", idFieldProperties);
        mappingProperties.put("localityPart.name", nameFieldProperties);
        mappingProperties.put("localityPart.type", typeFieldProperties);

        mappingProperties.put("street.id", idFieldProperties);
        mappingProperties.put("street.main_name", nameFieldProperties);
        mappingProperties.put("street.leading_name", nameFieldProperties);
        mappingProperties.put("street.type", typeFieldProperties);

        return mappingProperties;
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
            e.printStackTrace();
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
                .fields(searchFieldsWithBoost)
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
            exception.printStackTrace();
            throw new IllegalArgumentException("wrong");
        }
    }

    private boolean indexExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest(LOCATIONS_INDEX);
        return elasticClient.indices().exists(request, RequestOptions.DEFAULT);
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

}
