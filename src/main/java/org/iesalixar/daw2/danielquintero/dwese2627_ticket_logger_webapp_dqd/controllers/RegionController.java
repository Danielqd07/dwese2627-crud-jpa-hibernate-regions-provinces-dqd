package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.daos.RegionDAO;

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

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.mappers.RegionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/regions")
public class RegionController {

    private static final Logger logger =
            LoggerFactory.getLogger(RegionController.class);

    private final RegionDAO regionDAO;
    private final MessageSource messageSource;

    public RegionController(
            RegionDAO regionDAO,
            MessageSource messageSource) {

        this.regionDAO = regionDAO;
        this.messageSource = messageSource;
    }


    /*
     * LISTADO
     */
    @GetMapping
    public String listRegions(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            Long totalElements =
                    regionDAO.countRegions();

            int totalPages =
                    (int) Math.ceil(
                            (double) totalElements / size
                    );

            int offset =
                    page * size;

            List<Region> regions =
                    regionDAO.listRegionsPage(
                            page,
                            size,
                            sortField,
                            sortDir
                    );

            List<RegionDTO> regionDTOs =
                    RegionMapper.toDTOList(regions);

            String reverseSortDir =
                    sortDir.equalsIgnoreCase("asc")
                            ? "desc"
                            : "asc";

            model.addAttribute(
                    "listRegions",
                    regionDTOs
            );

            model.addAttribute(
                    "currentPage",
                    page
            );

            model.addAttribute(
                    "pageSize",
                    size
            );

            model.addAttribute(
                    "offset",
                    offset
            );

            model.addAttribute(
                    "totalPages",
                    totalPages
            );

            model.addAttribute(
                    "totalElements",
                    totalElements
            );

            model.addAttribute(
                    "sortField",
                    sortField
            );

            model.addAttribute(
                    "sortDir",
                    sortDir
            );

            model.addAttribute(
                    "reverseSortDir",
                    reverseSortDir
            );

            return "views/region/region-list";

        } catch (Exception e) {

            logger.error(
                    "Error al listar las regiones: {}",
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.list.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/";
        }
    }


    /*
     * FORMULARIO NUEVA REGIÓN
     */
    @GetMapping("/new")
    public String showNewForm(
            Model model) {

        model.addAttribute(
                "region",
                new RegionCreateDTO()
        );

        model.addAttribute(
                "isEdit",
                false
        );

        return "views/region/region-form";
    }


    /*
     * INSERTAR
     */
    @PostMapping("/insert")
    public String insertRegion(
            @Valid
            @ModelAttribute("region")
            RegionCreateDTO region,
            BindingResult result,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "isEdit",
                    false
            );

            return "views/region/region-form";
        }

        try {

            if (regionDAO.existsRegionByCode(
                    region.getCode())) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.region-controller.insert.codeExist",
                                null,
                                locale
                        );

                model.addAttribute(
                        "errorMessage",
                        errorMessage
                );

                model.addAttribute(
                        "isEdit",
                        false
                );

                return "views/region/region-form";
            }

            Region entity =
                    RegionMapper.toEntity(region);

            regionDAO.insertRegion(entity);

            return "redirect:/regions";

        } catch (Exception e) {

            logger.error(
                    "Error al insertar la región: {}",
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.insert.error",
                            null,
                            locale
                    );

            model.addAttribute(
                    "errorMessage",
                    errorMessage
            );

            model.addAttribute(
                    "isEdit",
                    false
            );

            return "views/region/region-form";
        }
    }


    /*
     * FORMULARIO EDITAR REGIÓN
     */
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            Region region =
                    regionDAO.getRegionById(id);

            if (region == null) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.region-controller.edit.notfound",
                                null,
                                locale
                        );

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        errorMessage
                );

                return "redirect:/regions";
            }

            RegionUpdateDTO regionDTO =
                    RegionMapper.toUpdateDTO(region);

            model.addAttribute(
                    "region",
                    regionDTO
            );

            model.addAttribute(
                    "isEdit",
                    true
            );

            return "views/region/region-form";

        } catch (Exception e) {

            logger.error(
                    "Error al cargar la región con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.edit.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/regions";
        }
    }


    /*
     * ACTUALIZAR
     */
    @PostMapping("/update")
    public String updateRegion(
            @Valid
            @ModelAttribute("region")
            RegionUpdateDTO region,
            BindingResult result,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "isEdit",
                    true
            );

            return "views/region/region-form";
        }

        try {

            if (regionDAO.existsRegionByCodeAndNotId(
                    region.getCode(),
                    region.getId())) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.region-controller.update.codeExist",
                                null,
                                locale
                        );

                model.addAttribute(
                        "errorMessage",
                        errorMessage
                );

                model.addAttribute(
                        "isEdit",
                        true
                );

                return "views/region/region-form";
            }

            Region entity =
                    RegionMapper.toEntity(region);

            regionDAO.updateRegion(entity);

            return "redirect:/regions";

        } catch (Exception e) {

            logger.error(
                    "Error al actualizar la región con ID {}: {}",
                    region.getId(),
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.update.error",
                            null,
                            locale
                    );

            model.addAttribute(
                    "errorMessage",
                    errorMessage
            );

            model.addAttribute(
                    "isEdit",
                    true
            );

            return "views/region/region-form";
        }
    }


    /*
     * ELIMINAR
     */
    @PostMapping("/delete")
    public String deleteRegion(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            regionDAO.deleteRegion(id);

        } catch (Exception e) {

            logger.error(
                    "Error al eliminar la región con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.delete.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );
        }

        return "redirect:/regions";
    }


    /*
     * DETALLE
     */
    @GetMapping("/detail")
    public String showDetail(
            @RequestParam("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            Region region =
                    regionDAO.getRegionById(id);

            if (region == null) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.region-controller.detail.notFound",
                                null,
                                locale
                        );

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        errorMessage
                );

                return "redirect:/regions";
            }

            RegionDetailDTO regionDTO =
                    RegionMapper.toDetailDTO(region);

            model.addAttribute(
                    "region",
                    regionDTO
            );

            return "views/region/region-detail";

        } catch (Exception e) {

            logger.error(
                    "Error al cargar el detalle de la región con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.region-controller.detail.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/regions";
        }
    }
}