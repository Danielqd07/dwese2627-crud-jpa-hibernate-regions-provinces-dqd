package org.iesalixar.daw2.danielquintero.dwese2627_ticket_logger_webapp_dqd.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDetailDTO {

    private Long id;
    private String code;
    private String name;
    private RegionDTO region;
}
