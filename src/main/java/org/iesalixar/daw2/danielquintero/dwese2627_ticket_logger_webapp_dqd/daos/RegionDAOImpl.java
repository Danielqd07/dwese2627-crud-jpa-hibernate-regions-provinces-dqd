package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RegionDAOImpl implements RegionDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(RegionDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;


    /*
     * LISTADO COMPLETO
     */
    @Override
    public List<Region> listAllRegions() {

        String query =
                "SELECT r FROM Region r";

        List<Region> regions =
                entityManager.createQuery(
                        query,
                        Region.class
                ).getResultList();

        logger.info(
                "Retrieved {} regions from the database",
                regions.size()
        );

        return regions;
    }


    /*
     * LISTADO PAGINADO Y ORDENADO
     */
    @Override
    public List<Region> listRegionsPage(
            int page,
            int size,
            String sortField,
            String sortDir) {

        int offset =
                page * size;

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Region> cq =
                cb.createQuery(Region.class);

        Root<Region> root =
                cq.from(Region.class);

        if ("desc".equalsIgnoreCase(sortDir)) {

            cq.orderBy(
                    cb.desc(
                            root.get(sortField)
                    )
            );

        } else {

            cq.orderBy(
                    cb.asc(
                            root.get(sortField)
                    )
            );
        }

        List<Region> regions =
                entityManager
                        .createQuery(cq)
                        .setFirstResult(offset)
                        .setMaxResults(size)
                        .getResultList();

        logger.info(
                "Retrieved page {} with {} regions ordered by {} {}",
                page,
                regions.size(),
                sortField,
                sortDir
        );

        return regions;
    }


    /*
     * CONTAR REGIONES
     */
    @Override
    public Long countRegions() {

        String query = """
                SELECT COUNT(r)
                FROM Region r
                """;

        return entityManager
                .createQuery(
                        query,
                        Long.class
                )
                .getSingleResult();
    }


    /*
     * INSERTAR
     */
    @Override
    public void insertRegion(
            Region region) {

        entityManager.persist(region);

        logger.info(
                "Inserted region with code {}",
                region.getCode()
        );
    }


    /*
     * ACTUALIZAR
     */
    @Override
    public void updateRegion(
            Region region) {

        entityManager.merge(region);

        logger.info(
                "Updated region with id {}",
                region.getId()
        );
    }


    /*
     * ELIMINAR
     */
    @Override
    public void deleteRegion(
            Long id) {

        Region region =
                entityManager.find(
                        Region.class,
                        id
                );

        if (region != null) {

            entityManager.remove(region);

            logger.info(
                    "Deleted region with id {}",
                    id
            );
        }
    }


    /*
     * BUSCAR POR ID
     */
    @Override
    public Region getRegionById(
            Long id) {

        Region region =
                entityManager.find(
                        Region.class,
                        id
                );

        if (region != null) {

            logger.info(
                    "Region retrieved: ID = {}, code = {}, name = {}",
                    region.getId(),
                    region.getCode(),
                    region.getName()
            );

        } else {

            logger.warn(
                    "No region found with id {}",
                    id
            );
        }

        return region;
    }


    /*
     * COMPROBAR SI EXISTE UN CÓDIGO
     */
    @Override
    public boolean existsRegionByCode(
            String code) {

        String query = """
                SELECT COUNT(r)
                FROM Region r
                WHERE UPPER(r.code) = :code
                """;

        Long count =
                entityManager.createQuery(
                                query,
                                Long.class
                        )
                        .setParameter(
                                "code",
                                code.toUpperCase()
                        )
                        .getSingleResult();

        return count > 0;
    }


    /*
     * COMPROBAR CÓDIGO EXCLUYENDO UN ID
     */
    @Override
    public boolean existsRegionByCodeAndNotId(
            String code,
            Long id) {

        String query = """
                SELECT COUNT(r)
                FROM Region r
                WHERE UPPER(r.code) = :code
                  AND r.id != :id
                """;

        Long count =
                entityManager.createQuery(
                                query,
                                Long.class
                        )
                        .setParameter(
                                "code",
                                code.toUpperCase()
                        )
                        .setParameter(
                                "id",
                                id
                        )
                        .getSingleResult();

        return count > 0;
    }
}