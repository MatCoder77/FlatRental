package com.flatrental.domain.announcement;

import com.flatrental.domain.announcement.address.Address_;
import com.flatrental.domain.announcement.bathroom.Bathroom_;
import com.flatrental.domain.announcement.kitchen.Kitchen_;
import com.flatrental.domain.announcement.room.Room;
import com.flatrental.domain.announcement.room.Room_;
import com.flatrental.domain.announcement.search.RoomCriteria;
import com.flatrental.domain.announcement.search.SearchCriteria;
import com.flatrental.domain.announcement.simpleattribute.SimpleAttribute_;
import com.flatrental.domain.announcement.simpleattribute.apartmentamenity.ApartmentAmenity;
import com.flatrental.domain.announcement.simpleattribute.furnishings.FurnishingItem;
import com.flatrental.domain.announcement.simpleattribute.neighbourhood.NeighbourhoodItem;
import com.flatrental.domain.announcement.simpleattribute.preferences.Preference;
import com.flatrental.domain.locations.abstractlocality.AbstractLocality_;
import com.flatrental.domain.locations.commune.Commune_;
import com.flatrental.domain.locations.district.District_;
import com.flatrental.domain.locations.street.Street_;
import com.flatrental.domain.locations.voivodeship.Voivodeship_;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.managedobject.ManagedObject_;
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
import java.util.stream.Stream;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

public class CustomAnnouncementRepositoryImpl implements CustomAnnouncementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Set<String> allowedSoringAttributes = Set.of(ManagedObject_.CREATED_AT, Announcement_.PRICE_PER_MONTH, Announcement_.QUALITY);

    @Override
    public Page<Announcement> searchAnnouncementsByCriteria(SearchCriteria sc, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Announcement> criteriaQuery = cb.createQuery(Announcement.class);
        Root<Announcement> announcement = criteriaQuery.from(Announcement.class);
        criteriaQuery.select(announcement);

        Predicate predicate = createPredicateBasedOnSearchCriteria(announcement, sc, cb, criteriaQuery);
        criteriaQuery.where(predicate);

        List<Order> sortingOrders = getSortingOrders(announcement, pageable.getSort(), cb);
        criteriaQuery.orderBy(sortingOrders);

        TypedQuery<Announcement> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, getTotalCount(predicate));
    }

    private Predicate createPredicateBasedOnSearchCriteria(Root<Announcement> announcement, SearchCriteria sc, CriteriaBuilder cb, CriteriaQuery<Announcement> criteriaQuery) {
        Path<Long> id = announcement.get(Announcement_.id);
        Path<AnnouncementType> type = announcement.get(Announcement_.type);
        Path<Integer> totalArea = announcement.get(Announcement_.totalArea);
        Path<Integer> numberOfRooms = announcement.get(Announcement_.numberOfRooms);
        Path<Integer> pricePerMonth = announcement.get(Announcement_.pricePerMonth);
        Path<Integer> additionalCosts = announcement.get(Announcement_.additionalCostsPerMonth);
        Path<Integer> securityDeposit = announcement.get(Announcement_.securityDeposit);
        Path<Integer> floor = announcement.get(Announcement_.floor);
        Path<Integer> maxFloorInBuilding = announcement.get(Announcement_.maxFloorInBuilding);
        Path<Long> buildingType = announcement.get(Announcement_.buildingType).get(SimpleAttribute_.id);
        Path<Long> buildingMaterial = announcement.get(Announcement_.buildingMaterial).get(SimpleAttribute_.id);
        Path<Long> heatingType = announcement.get(Announcement_.heatingType).get(SimpleAttribute_.id);
        Path<Long> windowType = announcement.get(Announcement_.windowType).get(SimpleAttribute_.id);
        Path<Long> parkingType = announcement.get(Announcement_.parkingType).get(SimpleAttribute_.id);
        Path<Long> apartmentState = announcement.get(Announcement_.apartmentState).get(SimpleAttribute_.id);
        Path<Integer> yearBuilt = announcement.get(Announcement_.yearBuilt);
        Path<Boolean> wellPlanned = announcement.get(Announcement_.wellPlanned);
        Expression<Set<ApartmentAmenity>> apartmentAmenities = announcement.get(Announcement_.apartmentAmenities);
        Path<Long> kitchenType = announcement.get(Announcement_.kitchen).get(Kitchen_.kitchenType).get(SimpleAttribute_.id);
        Path<Integer> kitchenArea = announcement.get(Announcement_.kitchen).get(Kitchen_.kitchenArea);
        Path<Long> cookerType = announcement.get(Announcement_.kitchen).get(Kitchen_.cookerType).get(SimpleAttribute_.id);
        Expression<Set<FurnishingItem>> kitchenFurnishing = announcement.get(Announcement_.kitchen).get(Kitchen_.furnishing);
        Path<Integer> numberOfBathrooms = announcement.get(Announcement_.bathroom).get(Bathroom_.numberOfBathrooms);
        Path<Boolean> separatedWC = announcement.get(Announcement_.bathroom).get(Bathroom_.separateWC);
        Expression<Set<FurnishingItem>> bathroomFurnishing = announcement.get(Announcement_.bathroom).get(Bathroom_.furnishing);
        Expression<Set<Preference>> preferences = announcement.get(Announcement_.preferences);
        Expression<Set<NeighbourhoodItem>> neighbourhood = announcement.get(Announcement_.neighbourhood);
        Path<Integer> numberOfFlatmates = announcement.get(Announcement_.numberOfFlatmates);
        Path<Long> author = announcement.get(ManagedObject_.createdBy).get(User_.id);
        Path<ManagedObjectState> managedObjectState = announcement.get(ManagedObject_.objectState);

        List<ApartmentAmenity> requiredApartmentAmenities = getAttrFromIds(sc.getRequiredApartmentAmenities(), ApartmentAmenity::new);

        List<Optional<Predicate>> predicates = Arrays.asList(
                getEqualsPredicate(id, sc.getId(), cb),
                getTypePredicate(type, sc.getAnnouncementType(), cb),
                getMinMaxPredicate(totalArea, sc.getMinTotalArea(), sc.getMaxTotalArea(), cb),
                getMinMaxPredicate(numberOfRooms, sc.getMinNumberOfRooms(), sc.getMaxNumberOfRooms(), cb),
                getMinMaxPredicate(pricePerMonth, sc.getMinPricePerMonth(), sc.getMaxPricePerMonth(), cb),
                getMinMaxPredicate(additionalCosts, sc.getMinAdditionalCostsPerMonth(), sc.getMaxAdditionalCostsPerMonth(), cb),
                getMinMaxPredicate(securityDeposit, sc.getMinSecurityDeposit(), sc.getMaxSecurityDeposit(), cb),
                getMinMaxPredicate(floor, sc.getMinFloor(), sc.getMaxFloor(), cb),
                getMinMaxPredicate(maxFloorInBuilding, sc.getMinMaxFloorInBuilding(), sc.getMaxMaxFloorInBuilding(), cb),
                getAddressPredicate(announcement, sc, cb),
                getAllowedAttrPredicate(buildingType, sc.getAllowedBuildingTypes()),
                getAllowedAttrPredicate(buildingMaterial, sc.getAllowedBuildingMaterials()),
                getAllowedAttrPredicate(heatingType, sc.getAllowedHeatingTypes()),
                getAllowedAttrPredicate(windowType, sc.getAllowedWindowTypes()),
                getAllowedAttrPredicate(parkingType, sc.getAllowedParkingTypes()),
                getAllowedAttrPredicate(apartmentState, sc.getAllowedApartmentStates()),
                getMinMaxPredicate(yearBuilt, sc.getMinYearBuilt(), sc.getMaxYearBuilt(), cb),
                getEqualsPredicate(wellPlanned, sc.getIsWellPlanned(), cb),
                getRequiredAttrPredicate(apartmentAmenities, requiredApartmentAmenities, cb),
                getRoomsPredicates(announcement, criteriaQuery, sc, cb),
                getAllowedAttrPredicate(kitchenType, sc.getAllowedKitchenTypes()),
                getMinMaxPredicate(kitchenArea, sc.getMinKitchenArea(), sc.getMaxKitchenArea(), cb),
                getAllowedAttrPredicate(cookerType, sc.getAllowedCookerTypes()),
                getRequiredAttrPredicate(kitchenFurnishing, getAttrFromIds(sc.getRequiredKitchenFurnishing(), FurnishingItem::new), cb),
                getMinMaxPredicate(numberOfBathrooms, sc.getMinNumberOfBathrooms(), sc.getMaxNumberOfBathrooms(), cb),
                getEqualsPredicate(separatedWC, sc.getHasSeparatedWC(), cb),
                getRequiredAttrPredicate(bathroomFurnishing, getAttrFromIds(sc.getRequiredBathroomFurnishing(), FurnishingItem::new), cb),
                getRequiredAttrPredicate(preferences, getAttrFromIds(sc.getRequiredPreferences(), Preference::new), cb),
                getRequiredAttrPredicate(neighbourhood, getAttrFromIds(sc.getRequiredNeighbourhoodItems(), NeighbourhoodItem::new), cb),
                getMinMaxPredicate(numberOfFlatmates, sc.getMinNumberOfFlatmates(), sc.getMaxNumberOfFlatmates(), cb),
                getEqualsPredicate(author, sc.getAuthor(), cb),
                getAllowedAttrPredicate(managedObjectState, sc.getAllowedManagedObjectStates())
        );

        return predicates.stream()
                .flatMap(Optional::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), predicatesList -> cb.and(predicatesList.toArray(new Predicate[0]))));
    }

    private Optional<Predicate> getTypePredicate(Path<AnnouncementType> typeAttribute, String announcementType, CriteriaBuilder criteriaBuilder) {
        return Optional.ofNullable(announcementType)
                .map(AnnouncementType::fromString)
                .map(type -> criteriaBuilder.equal(typeAttribute, type));
    }

    private <T extends Number> Optional<Predicate> getMinMaxPredicate(Path<T> attribute, T min, T max, CriteriaBuilder criteriaBuilder) {
        if (Objects.equals(min, max)) {
            return getEqualsPredicate(attribute, min, criteriaBuilder);
        }

        Optional<Predicate> greaterEqualPredicate = Optional.ofNullable(min)
                .map(val -> criteriaBuilder.ge(attribute, val));
        Optional<Predicate> lessEqualPredicate = Optional.ofNullable(max)
                .map(val -> criteriaBuilder.le(attribute, val));

        List<Predicate> predicates = Stream.of(greaterEqualPredicate, lessEqualPredicate)
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

    private <T> List<T> getAttrFromIds(Set<Long> ids, Function<Long, T> mapper) {
        return Optional.ofNullable(ids)
                .orElse(Collections.emptySet())
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    private <T> Optional<Predicate> getRequiredAttrPredicate(Expression<Set<T>> attributes, List<T> requiredAttributes, CriteriaBuilder criteriaBuilder) {
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


    private <T> Optional<Predicate> getAllowedAttrPredicate(Path<T> attribute, Set<T> allowedValues) {
        if (CollectionUtils.isEmpty(allowedValues)) {
            return Optional.empty();
        }
        return Optional.of(allowedValues).map(attribute::in);
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
                getMinMaxPredicate(announcementXroom.get(Room_.area), roomCriteria.getMinArea(), roomCriteria.getMaxArea(), criteriaBuilder),
                getMinMaxPredicate(announcementXroom.get(Room_.numberOfPersons), roomCriteria.getMinNumberOfPersons(), roomCriteria.getMaxNumberOfPersons(), criteriaBuilder),
                getMinMaxPredicate(announcementXroom.get(Room_.personsOccupied), roomCriteria.getMinPersonsOccupied(), roomCriteria.getMaxPersonsOccupied(), criteriaBuilder),
                getRequiredAttrPredicate(announcementXroom.get(Room_.furnishings), getAttrFromIds(roomCriteria.getRequiredFurnishing(), FurnishingItem::new), criteriaBuilder)
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
