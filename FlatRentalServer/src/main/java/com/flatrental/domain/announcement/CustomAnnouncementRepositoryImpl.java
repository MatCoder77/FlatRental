package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.address.Address_;
import com.flatrental.domain.announcement.search.RoomCriteria;
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
import com.flatrental.domain.locations.abstractlocality.AbstractLocality_;
import com.flatrental.domain.locations.commune.Commune_;
import com.flatrental.domain.locations.district.District_;
import com.flatrental.domain.locations.street.Street_;
import com.flatrental.domain.locations.voivodeship.Voivodeship_;
import com.flatrental.domain.user.User_;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
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
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

public class CustomAnnouncementRepositoryImpl implements CustomAnnouncementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Set allowedSoringAttributes = Set.of(Announcement_.CREATED_AT, Announcement_.PRICE_PER_MONTH, Announcement_.QUALITY);

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
                getAddressPredicate(a, searchCriteria, criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.buildingType).get(BuildingType_.id), searchCriteria.getAllowedBuildingTypes()),
                getAttributeInSetPredicate(a.get(Announcement_.buildingMaterial).get(BuildingMaterial_.id), searchCriteria.getAllowedBuildingMaterials()),
                getAttributeInSetPredicate(a.get(Announcement_.heatingType).get(HeatingType_.id), searchCriteria.getAllowedHeatingTypes()),
                getAttributeInSetPredicate(a.get(Announcement_.windowType).get(WindowType_.id), searchCriteria.getAllowedWindowTypes()),
                getAttributeInSetPredicate(a.get(Announcement_.parkingType).get(ParkingType_.id), searchCriteria.getAllowedParkingTypes()),
                getAttributeInSetPredicate(a.get(Announcement_.apartmentState).get(ApartmentState_.id), searchCriteria.getAllowedApartmentStates()),
                getNumberMinMaxPredicate(a.get(Announcement_.yearBuilt), searchCriteria.getMinYearBuilt(), searchCriteria.getMaxYearBuilt(), criteriaBuilder),
                getEqualsPredicate(a.get(Announcement_.wellPlanned), searchCriteria.getIsWellPlanned(), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.apartmentAmenities), getAttributesFromIds(searchCriteria.getRequiredApartmentAmenities(), ApartmentAmenity::fromId), criteriaBuilder),
                getRoomsPredicates(a, criteriaQuery, searchCriteria, criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.kitchen).get(Kitchen_.kitchenType).get(KitchenType_.id), searchCriteria.getAllowedKitchenTypes()),
                getNumberMinMaxPredicate(a.get(Announcement_.kitchen).get(Kitchen_.kitchenArea), searchCriteria.getMinKitchenArea(), searchCriteria.getMaxKitchenArea(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.kitchen).get(Kitchen_.cookerType).get(CookerType_.id), searchCriteria.getAllowedCookerTypes()),
                getRequiredAttributesPredicate(a.get(Announcement_.kitchen).get(Kitchen_.furnishing), getAttributesFromIds(searchCriteria.getRequiredKitchenFurnishing(), FurnishingItem::fromId), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.bathroom).get(Bathroom_.numberOfBathrooms), searchCriteria.getMinNumberOfBathrooms(), searchCriteria.getMaxNumberOfBathrooms(), criteriaBuilder),
                getEqualsPredicate(a.get(Announcement_.bathroom).get(Bathroom_.separateWC), searchCriteria.getHasSeparatedWC(), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.bathroom).get(Bathroom_.furnishing), getAttributesFromIds(searchCriteria.getRequiredBathroomFurnishing(), FurnishingItem::fromId), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.preferences), getAttributesFromIds(searchCriteria.getRequiredPreferences(), Preference::fromId), criteriaBuilder),
                getRequiredAttributesPredicate(a.get(Announcement_.neighbourhood), getAttributesFromIds(searchCriteria.getRequiredNeighbourhoodItems(), NeighbourhoodItem::fromId), criteriaBuilder),
                getNumberMinMaxPredicate(a.get(Announcement_.numberOfFlatmates), searchCriteria.getMinNumberOfFlatmates(), searchCriteria.getMaxNumberOfFlatmates(), criteriaBuilder),
                getEqualsPredicate(a.get(Announcement_.createdBy).get(User_.id), searchCriteria.getAuthor(), criteriaBuilder),
                getAttributeInSetPredicate(a.get(Announcement_.objectState), searchCriteria.getAllowedManagedObjectStates())

        );

        Predicate predicate = predicates.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), predicatesList -> criteriaBuilder.and(predicatesList.toArray(new Predicate[0]))));

        criteriaQuery.where(predicate);

        List<Order> sortingOrders = getSortingOrders(a, pageable.getSort(), criteriaBuilder);

        criteriaQuery.orderBy(sortingOrders);

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

    private Optional<Predicate> getAddressPredicate(Root<Announcement> a, SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        Path<Long> voivodeshipIdPath = a.get(Announcement_.address).get(Address_.voivodeship).get(Voivodeship_.id);
        Path<Long> districtIdPath = a.get(Announcement_.address).get(Address_.district).get(District_.id);
        Path<Long> communeIdPath = a.get(Announcement_.address).get(Address_.commune).get(Commune_.id);
        Path<Long> localityIdPath = a.get(Announcement_.address).get(Address_.locality).get(AbstractLocality_.id);
        Path<Long> localityDistrictIdPath = a.get(Announcement_.address).get(Address_.localityDistrict).get(AbstractLocality_.id);
        Path<Long> localityPartIdPath = a.get(Announcement_.address).get(Address_.localityPart).get(AbstractLocality_.id);
        Path<Long> streetIdPath = a.get(Announcement_.address).get(Address_.street).get(Street_.id);

        List<Optional<Predicate>> addressPredicates = Arrays.asList(
                getEqualsPredicate(voivodeshipIdPath, searchCriteria.getVoivodeshipId(), criteriaBuilder),
                getEqualsPredicate(districtIdPath, searchCriteria.getDistrictId(),criteriaBuilder),
                getEqualsPredicate(communeIdPath, searchCriteria.getCommuneId(), criteriaBuilder),
                getEqualsPredicate(localityIdPath, searchCriteria.getLocalityId(), criteriaBuilder),
                getEqualsPredicate(localityDistrictIdPath, searchCriteria.getLocalityDistrictId(), criteriaBuilder),
                getEqualsPredicate(localityPartIdPath, searchCriteria.getLocalityPartId(), criteriaBuilder),
                getEqualsPredicate(streetIdPath, searchCriteria.getStreetId(), criteriaBuilder));

        return Optional.ofNullable(addressPredicates.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> criteriaBuilder.and(list.toArray(new Predicate[0])))));
    }


    private <T> Optional<Predicate> getAttributeInSetPredicate(Path<T> attribute, Set<T> allowedValues) {
        if (CollectionUtils.isEmpty(allowedValues)) {
            return Optional.empty();
        }
        return Optional.ofNullable(allowedValues).map(values -> attribute.in(values));
    }

    private Optional<Predicate> getRoomsPredicates(Root<Announcement> a, CriteriaQuery<Announcement> criteriaQuery,
                                                   SearchCriteria searchCriteria, CriteriaBuilder criteriaBuilder) {
        List<Predicate> specifiedRoomPredicates = Optional.ofNullable(searchCriteria.getRooms())
                .orElse(Collections.emptySet())
                .stream()
                .map(roomCriteria -> getPredicateForRoom(a, criteriaQuery, roomCriteria, criteriaBuilder))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        if (specifiedRoomPredicates.isEmpty()) {
            return Optional.empty();
        }

        Predicate predicate = criteriaBuilder.and(specifiedRoomPredicates.toArray(new Predicate[0]));
        return Optional.ofNullable(predicate);
    }

    private Optional<Predicate> getPredicateForRoom(Root<Announcement> a, CriteriaQuery<Announcement> criteriaQuery, RoomCriteria roomCriteria, CriteriaBuilder criteriaBuilder) {
        Subquery<Room> roomSubquery = criteriaQuery.subquery(Room.class);
        Root<Announcement> subRoot = roomSubquery.correlate(a);
        Join<Announcement, Room> announcementXroom = subRoot.join(Announcement_.rooms);
        roomSubquery.select(announcementXroom);
        List<Optional<Predicate>> roomPredicates = Arrays.asList(
                getNumberMinMaxPredicate(announcementXroom.get(Room_.area), roomCriteria.getMinArea(), roomCriteria.getMaxArea(), criteriaBuilder),
                getNumberMinMaxPredicate(announcementXroom.get(Room_.numberOfPersons), roomCriteria.getMinNumberOfPersons(), roomCriteria.getMaxNumberOfPersons(), criteriaBuilder),
                getNumberMinMaxPredicate(announcementXroom.get(Room_.personsOccupied), roomCriteria.getMinPersonsOccupied(), roomCriteria.getMaxPersonsOccupied(), criteriaBuilder),
                getRequiredAttributesPredicate(announcementXroom.get(Room_.furnishings), getAttributesFromIds(roomCriteria.getRequiredFurnishing(), FurnishingItem::fromId), criteriaBuilder)
        );
        List<Predicate> specifiedRoomPredicates = roomPredicates.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        if (specifiedRoomPredicates.isEmpty()) {
            return Optional.empty();
        }

        Predicate predicate = criteriaBuilder.and(specifiedRoomPredicates.toArray(new Predicate[0]));

        roomSubquery.where(predicate);

        return Optional.ofNullable(criteriaBuilder.exists(roomSubquery));
    }

    private Long getTotalCount(Predicate predicate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Announcement> a = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(criteriaBuilder.count(a))
                .where(predicate);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<Order> getSortingOrders(Root<Announcement> a, Sort sort, CriteriaBuilder criteriaBuilder) {
        return sort.get()
                .filter(order -> isAllowedSortingAttribute(order.getProperty()))
                .map(order -> mapToCriteriaOrder(a, order, criteriaBuilder))
                .collect(Collectors.toList());
    }

    private boolean isAllowedSortingAttribute(String attributeName) {
        return allowedSoringAttributes.contains(attributeName);
    }

    private Order mapToCriteriaOrder(Root<Announcement> a, Sort.Order order, CriteriaBuilder criteriaBuilder) {
        if (order.isAscending()) {
            return criteriaBuilder.asc(a.get(order.getProperty()));
        }
        return criteriaBuilder.desc(a.get(order.getProperty()));
    }

}
