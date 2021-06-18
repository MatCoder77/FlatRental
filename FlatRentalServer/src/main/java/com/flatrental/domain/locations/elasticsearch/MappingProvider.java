package com.flatrental.domain.locations.elasticsearch;

import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MappingProvider {

    public static final String LOCATIONS_INDEX = "locations";
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
    public static final Map<String, Float> SEARCH_FIELDS_WITH_BOOST = Map.of(
            "voivodeship.name", 1.0f,
            "district.name", 1.0f,
            "commune.name", 1.0f,
            "locality.name", 8.0f,
            "localityDistrict.name", 1.0f,
            "localityPart.name", 1.0f,
            "street.main_name", 1.0f,
            "street.leading_name", 1.0f);

    public CreateIndexRequest getCreateRequestForLocationIndex() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(LOCATIONS_INDEX);
        createIndexRequest.settings(createLocationsIndexSettings());
        createIndexRequest.mapping(createLocationsIndexMapping());
        return createIndexRequest;
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
        edgeNGramFilterProperties.put(TYPE_PROPERTY, "edge_ngram");
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

}
