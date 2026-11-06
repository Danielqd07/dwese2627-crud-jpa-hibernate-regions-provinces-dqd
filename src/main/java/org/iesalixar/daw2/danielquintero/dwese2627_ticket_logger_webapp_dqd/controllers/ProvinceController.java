package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.daos.ProvinceDAO;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.daos.RegionDAO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceDTO;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceDetailDTO;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.ProvinceUpdateDTO;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.dtos.RegionDTO;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Province;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Region;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.mappers.ProvinceMapper;
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
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProvinceController.class);

    private final ProvinceDAO provinceDAO;
    private final RegionDAO regionDAO;
    private final MessageSource messageSource;

    public ProvinceController(
            ProvinceDAO provinceDAO,
            RegionDAO regionDAO,
            MessageSource messageSource) {

        this.provinceDAO = provinceDAO;
        this.regionDAO = regionDAO;
        this.messageSource = messageSource;
    }


    /*
     * LISTADO
     */
    @GetMapping
    public String listProvinces(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            Long totalElements =
                    provinceDAO.countProvinces();

            int totalPages =
                    (int) Math.ceil(
                            (double) totalElements / size
                    );

            int offset =
                    page * size;

            List<Province> provinces =
                    provinceDAO.listProvincesPage(
                            page,
                            size,
                            sortField,
                            sortDir
                    );

            List<ProvinceDTO> provinceDTOs =
                    ProvinceMapper.toDTOList(provinces);

            String reverseSortDir =
                    sortDir.equalsIgnoreCase("asc")
                            ? "desc"
                            : "asc";

            model.addAttribute(
                    "listProvinces",
                    provinceDTOs
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

            return "views/province/province-list";

        } catch (Exception e) {

            logger.error(
                    "Error al listar las provincias: {}",
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.list.error",
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
     * FORMULARIO NUEVA PROVINCIA
     */
    @GetMapping("/new")
    public String showNewForm(
            Model model) {

        model.addAttribute(
                "province",
                new ProvinceCreateDTO()
        );

        model.addAttribute(
                "isEdit",
                false
        );

        loadRegions(model);

        return "views/province/province-form";
    }


    /*
     * INSERTAR
     */
    @PostMapping("/insert")
    public String insertProvince(
            @Valid
            @ModelAttribute("province")
            ProvinceCreateDTO province,
            BindingResult result,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "isEdit",
                    false
            );

            loadRegions(model);

            return "views/province/province-form";
        }

        try {

            if (provinceDAO.existsProvinceByCode(
                    province.getCode())) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.province-controller.insert.codeExist",
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

                loadRegions(model);

                return "views/province/province-form";
            }

            Province entity =
                    ProvinceMapper.toEntity(province);

            provinceDAO.insertProvince(entity);

            return "redirect:/provinces";

        } catch (Exception e) {

            logger.error(
                    "Error al insertar la provincia: {}",
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.insert.error",
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

            loadRegions(model);

            return "views/province/province-form";
        }
    }


    /*
     * FORMULARIO EDITAR
     */
    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            Province province =
                    provinceDAO.getProvinceById(id);

            if (province == null) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.province-controller.edit.notfound",
                                null,
                                locale
                        );

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        errorMessage
                );

                return "redirect:/provinces";
            }

            ProvinceUpdateDTO provinceDTO =
                    ProvinceMapper.toUpdateDTO(province);

            model.addAttribute(
                    "province",
                    provinceDTO
            );

            model.addAttribute(
                    "isEdit",
                    true
            );

            loadRegions(model);

            return "views/province/province-form";

        } catch (Exception e) {

            logger.error(
                    "Error al cargar la provincia con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.edit.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/provinces";
        }
    }


    /*
     * ACTUALIZAR
     */
    @PostMapping("/update")
    public String updateProvince(
            @Valid
            @ModelAttribute("province")
            ProvinceUpdateDTO province,
            BindingResult result,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "isEdit",
                    true
            );

            loadRegions(model);

            return "views/province/province-form";
        }

        try {

            if (provinceDAO.existsProvinceByCodeAndNotId(
                    province.getCode(),
                    province.getId())) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.province-controller.update.codeExist",
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

                loadRegions(model);

                return "views/province/province-form";
            }

            Province entity =
                    ProvinceMapper.toEntity(province);

            provinceDAO.updateProvince(entity);

            return "redirect:/provinces";

        } catch (Exception e) {

            logger.error(
                    "Error al actualizar la provincia con ID {}: {}",
                    province.getId(),
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.update.error",
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

            loadRegions(model);

            return "views/province/province-form";
        }
    }


    /*
     * ELIMINAR
     */
    @PostMapping("/delete")
    public String deleteProvince(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        try {

            provinceDAO.deleteProvince(id);

        } catch (Exception e) {

            logger.error(
                    "Error al eliminar la provincia con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.delete.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );
        }

        return "redirect:/provinces";
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

            Province province =
                    provinceDAO.getProvinceById(id);

            if (province == null) {

                String errorMessage =
                        messageSource.getMessage(
                                "msg.province-controller.detail.notFound",
                                null,
                                locale
                        );

                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        errorMessage
                );

                return "redirect:/provinces";
            }

            ProvinceDetailDTO provinceDTO =
                    ProvinceMapper.toDetailDTO(province);

            model.addAttribute(
                    "province",
                    provinceDTO
            );

            return "views/province/province-detail";

        } catch (Exception e) {

            logger.error(
                    "Error al cargar el detalle de la provincia con ID {}: {}",
                    id,
                    e.getMessage()
            );

            String errorMessage =
                    messageSource.getMessage(
                            "msg.province-controller.detail.error",
                            null,
                            locale
                    );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/provinces";
        }
    }


    /*
     * CARGAR REGIONES PARA EL SELECT
     */
    private void loadRegions(Model model) {

        try {

            List<Region> regions =
                    regionDAO.listAllRegions();

            List<RegionDTO> regionDTOs =
                    RegionMapper.toDTOList(regions);

            model.addAttribute(
                    "listRegions",
                    regionDTOs
            );

        } catch (Exception e) {

            logger.error(
                    "Error al cargar las regiones: {}",
                    e.getMessage()
            );

            throw new RuntimeException(
                    "Error al cargar las regiones",
                    e
            );
        }
    }
}