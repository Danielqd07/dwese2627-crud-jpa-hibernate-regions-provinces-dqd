package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.mappers;

import lombok.experimental.UtilityClass;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.RegionCreateDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.RegionDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.RegionDetailDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.RegionUpdateDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Region;

import java.util.List;

@UtilityClass
public class RegionMapper {

    /*
     * Entity -> RegionDTO
     */
    public RegionDTO toDTO(
            Region region) {

        if (region == null) {
            return null;
        }

        return new RegionDTO(
                region.getId(),
                region.getCode(),
                region.getName()
        );
    }


    /*
     * List<Region> -> List<RegionDTO>
     */
    public List<RegionDTO> toDTOList(
            List<Region> regions) {

        if (regions == null) {
            return List.of();
        }

        return regions.stream()
                .map(RegionMapper::toDTO)
                .toList();
    }


    /*
     * Entity -> RegionDetailDTO
     */
    public RegionDetailDTO toDetailDTO(
            Region region) {

        if (region == null) {
            return null;
        }

        List<ProvinceDTO> provinces =
                region.getProvinces() == null
                        ? List.of()
                        : region.getProvinces()
                        .stream()
                        .map(province ->
                                new ProvinceDTO(
                                        province.getId(),
                                        province.getCode(),
                                        province.getName(),
                                        province.getRegion() != null
                                                ? province.getRegion().getName()
                                                : null
                                )
                        )
                        .toList();

        return new RegionDetailDTO(
                region.getId(),
                region.getCode(),
                region.getName(),
                provinces
        );
    }


    /*
     * Entity -> RegionUpdateDTO
     */
    public RegionUpdateDTO toUpdateDTO(
            Region region) {

        if (region == null) {
            return null;
        }

        return new RegionUpdateDTO(
                region.getId(),
                region.getCode(),
                region.getName()
        );
    }


    /*
     * RegionCreateDTO -> Entity
     */
    public Region toEntity(
            RegionCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        Region region = new Region();

        region.setCode(
                dto.getCode()
        );

        region.setName(
                dto.getName()
        );

        return region;
    }


    /*
     * RegionUpdateDTO -> Entity
     */
    public Region toEntity(
            RegionUpdateDTO dto) {

        if (dto == null) {
            return null;
        }

        Region region = new Region();

        region.setId(
                dto.getId()
        );

        region.setCode(
                dto.getCode()
        );

        region.setName(
                dto.getName()
        );

        return region;
    }
}