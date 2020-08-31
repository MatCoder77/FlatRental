package com.flatrental.domain.announcement.room;

import com.flatrental.api.announcement.RoomBrowseDTO;
import com.flatrental.api.announcement.RoomDTO;
import com.flatrental.api.simpleattribute.SimpleAttributeDTO;
import com.flatrental.domain.simpleattribute.SimpleAttributeMapper;
import com.flatrental.domain.simpleattribute.furnishings.FurnishingItem;
import com.flatrental.domain.simpleattribute.furnishings.FurnishingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomMapper {

    private final FurnishingService furnishingService;
    private final SimpleAttributeMapper simpleAttributeMapper;

    public Set<Room> mapToRooms(Collection<RoomDTO> roomDTOs) {
        return Optional.ofNullable(roomDTOs)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToRoom)
                .collect(Collectors.toSet());
    }

    public Room mapToRoom(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }
        return Room.builder()
                .numberOfPersons(roomDTO.getNumberOfPersons())
                .personsOccupied(roomDTO.getPersonsOccupied())
                .area(roomDTO.getArea())
                .pricePerMonth(roomDTO.getPricePerMonth())
                .furnishings(mapToFurnishing(roomDTO.getFurnishing()))
                .roomNumber(roomDTO.getRoomNumber())
                .build();
    }

    private Set<FurnishingItem> mapToFurnishing(List<SimpleAttributeDTO> furnishingDTOs) {
        return simpleAttributeMapper.mapToSimpleAttributes(furnishingDTOs, furnishingService::getFurnishingItems);
    }

    public List<RoomDTO> mapToRoomDTOs(Collection<Room> rooms) {
        return Optional.ofNullable(rooms)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToRoomDTO)
                .collect(Collectors.toList());
    }

    public RoomDTO mapToRoomDTO(Room room) {
        if (room == null) {
            return null;
        }
        return RoomDTO.builder()
                .id(room.getId())
                .numberOfPersons(room.getNumberOfPersons())
                .personsOccupied(room.getPersonsOccupied())
                .area(room.getArea())
                .pricePerMonth(room.getPricePerMonth())
                .furnishing(mapToFurnishingItemDTOs(room.getFurnishings()))
                .roomNumber(room.getRoomNumber())
                .build();
    }

    private List<SimpleAttributeDTO> mapToFurnishingItemDTOs(Collection<FurnishingItem> furnishingItems) {
        return simpleAttributeMapper.mapToSimpleAttributeDTOs(furnishingItems);
    }

    public List<RoomBrowseDTO> mapToRoomBrowseDTOs(Collection<Room> rooms) {
        return Optional.ofNullable(rooms)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::mapToRoomBrowseDTO)
                .collect(Collectors.toList());
    }
    private RoomBrowseDTO mapToRoomBrowseDTO(Room room) {
        return RoomBrowseDTO.builder()
                .id(room.getId())
                .numberOfPersons(room.getNumberOfPersons())
                .area(room.getArea())
                .build();
    }

}
