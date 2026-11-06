package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.mappers;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceCreateDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceDetailDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceUpdateDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Province;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Region;

import java.util.List;

public class ProvinceMapper {

    private ProvinceMapper() {
    }


    /*
     * Province -> ProvinceDTO
     */
    public static ProvinceDTO toDTO(
            Province province) {

        if (province == null) {
            return null;
        }

        return new ProvinceDTO(
                province.getId(),
                province.getCode(),
                province.getName(),
                province.getRegion() != null
                        ? province.getRegion().getName()
                        : null
        );
    }


    /*
     * List<Province> -> List<ProvinceDTO>
     */
    public static List<ProvinceDTO> toDTOList(
            List<Province> provinces) {

        if (provinces == null) {
            return List.of();
        }

        return provinces.stream()
                .map(ProvinceMapper::toDTO)
                .toList();
    }


    /*
     * Province -> ProvinceDetailDTO
     */
    public static ProvinceDetailDTO toDetailDTO(
            Province province) {

        if (province == null) {
            return null;
        }

        return new ProvinceDetailDTO(
                province.getId(),
                province.getCode(),
                province.getName(),
                RegionMapper.toDTO(
                        province.getRegion()
                )
        );
    }


    /*
     * Province -> ProvinceUpdateDTO
     */
    public static ProvinceUpdateDTO toUpdateDTO(
            Province province) {

        if (province == null) {
            return null;
        }

        return new ProvinceUpdateDTO(
                province.getId(),
                province.getCode(),
                province.getName(),
                province.getRegion() != null
                        ? province.getRegion().getId()
                        : null
        );
    }


    /*
     * ProvinceCreateDTO -> Province
     */
    public static Province toEntity(
            ProvinceCreateDTO dto) {

        if (dto == null) {
            return null;
        }

        Province province =
                new Province();

        province.setCode(
                dto.getCode()
        );

        province.setName(
                dto.getName()
        );

        if (dto.getRegionId() != null) {

            Region region =
                    new Region();

            region.setId(
                    dto.getRegionId()
            );

            province.setRegion(
                    region
            );
        }

        return province;
    }


    /*
     * ProvinceUpdateDTO -> Province
     */
    public static Province toEntity(
            ProvinceUpdateDTO dto) {

        if (dto == null) {
            return null;
        }

        Province province =
                new Province();

        province.setId(
                dto.getId()
        );

        province.setCode(
                dto.getCode()
        );

        province.setName(
                dto.getName()
        );

        if (dto.getRegionId() != null) {

            Region region =
                    new Region();

            region.setId(
                    dto.getRegionId()
            );

            province.setRegion(
                    region
            );
        }

        return province;
    }
}