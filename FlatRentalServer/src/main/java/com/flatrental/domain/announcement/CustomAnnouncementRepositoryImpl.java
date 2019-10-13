package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.announcement.simpleattributes.apartmentamenities.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattributes.apartmentstate.ApartmentState_;
import com.flatrental.domain.announcement.simpleattributes.buildingmaterial.BuildingMaterial_;
import com.flatrental.domain.announcement.simpleattributes.buildingtype.BuildingType_;
import com.flatrental.domain.announcement.simpleattributes.cookertype.CookerType_;
import com.flatrental.domain.announcement.simpleattributes.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.simpleattributes.heatingtype.HeatingType_;
import com.flatrental.domain.announcement.simpleattributes.kitchentype.KitchenType_;
import com.flatrental.domain.announcement.simpleattributes.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.announcement.simpleattributes.parkingtype.ParkingType_;
import com.flatrental.domain.announcement.simpleattributes.preferences.Preference;
import com.flatrental.domain.announcement.simpleattributes.windowtype.WindowType_;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

public class CustomAnnouncementRepositoryImpl implements CustomAnnouncementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Announcement> searchAnnouncementsByCriteria(SearchCriteria searchCriteria, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Announcement> criteriaQuery = criteriaBuilder.createQuery(Announcement.class);
        Root<Announcement> a = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(a);
        Expression<Set<ApartmentAmenity>> setExpression = a.get(Announcement_.apartmentAmenities);
        criteriaBuilder.isMember(ApartmentAmenity.fromId(2L), setExpression);
        List<Optional<Predicate>> predicates = Arrays.asList(
                getEqualsPredicate(a.get(Announcement_.id), searchCriteria.getId(), criteriaBuilder),
                getTypePredicate(a.get(Announcement_.type), searchCriteria.getAnnouncementType(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.totalArea), searchCriteria.getMinTotalArea(), searchCriteria.getMaxTotalArea(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.numberOfRooms), searchCriteria.getMinNumberOfRooms(), searchCriteria.getMaxNumberOfRooms(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.pricePerMonth), searchCriteria.getMinPricePerMonth(), searchCriteria.getMaxPricePerMonth(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.additionalCostsPerMonth), searchCriteria.getMinAdditionalCostsPerMonth(), searchCriteria.getMaxAdditionalCostsPerMonth(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.securityDeposit), searchCriteria.getMinSecurityDeposit(), searchCriteria.getMaxSecurityDeposit(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.floor), searchCriteria.getMinFloor(), searchCriteria.getMaxFloor(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.maxFloorInBuilding), searchCriteria.getMinMaxFloorInBuilding(), searchCriteria.getMaxMaxFloorInBuilding(), criteriaBuilder),
                //availableFrom
                //address
                getAttributeInSetPredicate(a.get(Announcement_.buildingType).get(BuildingType_.id), searchCriteria.getAllowedBuildingTypes(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.buildingMaterial).get(BuildingMaterial_.id), searchCriteria.getAllowedBuildingMaterials(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.heatingType).get(HeatingType_.id), searchCriteria.getAllowedHeatingTypes(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.windowType).get(WindowType_.id), searchCriteria.getAllowedWindowTypes(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.parkingType).get(ParkingType_.id), searchCriteria.getAllowedParkingTypes(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.apartmentState).get(ApartmentState_.id), searchCriteria.getAllowedApartmentStates(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.yearBuilt), searchCriteria.getMinYearBuilt(), searchCriteria.getMaxYearBuilt(), criteriaBuilder),
                getEqualsPredicate(a.get(Announcement_.wellPlanned), searchCriteria.getIsWellPlanned(), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.apartmentAmenities), getAttributesFromIds(searchCriteria.getRequiredApartmentAmenities(), ApartmentAmenity::fromId), criteriaBuilder),
                //rooms
                getAttributeInSetPredicate(a.get(Announcement_.kitchen).get(Kitchen_.kitchenType).get(KitchenType_.id), searchCriteria.getAllowedKitchenTypes(), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.kitchen).get(Kitchen_.kitchenArea), searchCriteria.getMinKitchenArea(), searchCriteria.getMaxKitchenArea(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.kitchen).get(Kitchen_.cookerType).get(CookerType_.id), searchCriteria.getAllowedCookerTypes(), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.kitchen).get(Kitchen_.furnishing), getAttributesFromIds(searchCriteria.getRequiredKitchenFurnishing(), FurnishingItem::fromId), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.bathroom).get(Bathroom_.numberOfBathrooms), searchCriteria.getMinNumberOfBathrooms(), searchCriteria.getMaxNumberOfBathrooms(), criteriaBuilder),
                getEqualsPredicate(a.get(Announcement_.bathroom).get(Bathroom_.separateWC), searchCriteria.getHasSeparatedWC(), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.bathroom).get(Bathroom_.furnishing), getAttributesFromIds(searchCriteria.getRequiredBathroomFurnishing(), FurnishingItem::fromId), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.preferences), getAttributesFromIds(searchCriteria.getRequiredPreferences(), Preference::fromId), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.neighbourhood), getAttributesFromIds(searchCriteria.getRequiredNeighbourhoodItems(), NeighbourhoodItem::fromId), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.numberOfFlatmates), searchCriteria.getMinNumberOfFlatmates(), searchCriteria.getMaxNumberOfFlatmates(), criteriaBuilder)

        );

        Predicate predicate = predicates.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), predicatesList -> criteriaBuilder.and(predicatesList.toArray(new Predicate[0]))));

        criteriaQuery.where(predicate);

        TypedQuery<Announcement> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, getTotalCount(predicate));

    }

    private Optional<Predicate> getTypePredicate(Path<AnnouncementType> typeAttribute, String announcementType, CriteriaBuilder criteriaBuilder) {
        return Optional.ofNullable(announcementType)
                .map(AnnouncementType::fromString)
                .map(type -> criteriaBuilder.equal(typeAttribute, type));
    }

    private <T extends Number> Optional<Predicate> getNumberMinMaxPredicate(Path<T> attribute, T min, T max, CriteriaBuilder criteriaBuilder) {
        if (Objects.equals(min, max)) {
            return getEqualsPredicate(attribute, min, criteriaBuilder);
        }

        Optional<Predicate> greaterEqualPredicate = Optional.ofNullable(min)
                .map(val -> criteriaBuilder.ge(attribute, val));
        Optional<Predicate> lessEqualPredicate = Optional.ofNullable(max)
                .map(val -> criteriaBuilder.le(attribute, val));

        List<Predicate> predicates = Arrays.asList(greaterEqualPredicate, lessEqualPredicate).stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        if (!predicates.isEmpty()) {
            return Optional.ofNullable(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }
        return Optional.empty();
    }

    private <T> Optional<Predicate> getEqualsPredicate(Path<T> attribute, T value, CriteriaBuilder criteriaBuilder) {
        return Optional.ofNullable(value)
                .map(val -> criteriaBuilder.equal(attribute, val));
    }

    private <T> List<T> getAttributesFromIds(Set<Long> ids, Function<Long, T> mapper) {
        return Optional.ofNullable(ids)
                .orElse(Collections.emptySet())
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private <T> Optional<Predicate> getRequiredAttributesPredicate(Expression<Set<T>> attributes, List<T> requiredAttributes, CriteriaBuilder criteriaBuilder) {
        if (requiredAttributes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(requiredAttributes.stream()
                .map(apartmentAmenity -> criteriaBuilder.isMember(apartmentAmenity, attributes))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> criteriaBuilder.and(list.toArray(new Predicate[0])))));
    }

//    private Optional<Predicate> getAddressPredicate(Root<Announcement> announcement, CriteriaBuilder criteriaBuilder) {
//        Root<ApartmentAmenity> amenityRoot = announcement.join(Announcement_.apartmentAmenities);
//    }

    private Optional<Predicate> getAttributeInSetPredicate(Path<Long> attribute, Set<Long> allowedValues, CriteriaBuilder criteriaBuilder) {
        if (CollectionUtils.isEmpty(allowedValues)) {
            return Optional.empty();
        }
        return Optional.ofNullable(allowedValues).map(values -> attribute.in(values));
    }

    private Long getTotalCount(Predicate predicate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Announcement> a = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(criteriaBuilder.count(a))
                .where(predicate);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }



}
