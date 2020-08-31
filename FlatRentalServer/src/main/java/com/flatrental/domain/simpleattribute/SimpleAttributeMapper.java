package com.flatrental.domain.simpleattribute;

import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

@Service
public class SimpleAttributeMapper {

    public List<SimpleAttributeDTO> mapToSimpleAttributeDTOs(Collection<? extends SimpleAttribute> simpleAttributes) {
        return Optional.ofNullable(simpleAttributes)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::mapToSimpleAttributeDTO)
                .collect(Collectors.toList());
    }

    public SimpleAttributeDTO mapToSimpleAttributeDTO(SimpleAttribute simpleAttribute) {
        if (simpleAttribute == null) {
            return null;
        }
        return new SimpleAttributeDTO(simpleAttribute.getId(), simpleAttribute.getName());
    }

    public <T extends SimpleAttribute> T mapToSimpleAttribute(SimpleAttributeDTO simpleAttributeDTO, LongFunction<T> attributeGetter) {
        return Optional.ofNullable(simpleAttributeDTO)
                .map(SimpleAttributeDTO::getId)
                .map(attributeGetter::apply)
                .orElse(null);
    }

    public <T extends SimpleAttribute> Set<T> mapToSimpleAttributes(Collection<? extends SimpleAttributeDTO> simpleAttributeDTOs, Function<Collection<Long>, List<T>> attributeGetter) {
        List<T> attributes = Optional.ofNullable(simpleAttributeDTOs)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(SimpleAttributeDTO::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), attributeGetter::apply));
        return new HashSet<>(attributes);
    }

}
