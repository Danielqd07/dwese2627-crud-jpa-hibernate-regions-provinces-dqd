package org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.iesalixar.daw2.danielquintero
        .dwese2627_ticket_logger_webapp_dqd.entities.Province;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProvinceDAOImpl implements ProvinceDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(ProvinceDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Province> listAllProvinces() {

        String query = """
                SELECT p
                FROM Province p
                JOIN FETCH p.region
                """;

        List<Province> provinces =
                entityManager.createQuery(
                        query,
                        Province.class
                ).getResultList();

        logger.info(
                "Retrieved {} provinces from the database",
                provinces.size()
        );

        return provinces;
    }

    @Override
    public void insertProvince(
            Province province) {

        entityManager.persist(province);

        logger.info(
                "Inserted province with code {}",
                province.getCode()
        );
    }

    @Override
    public Province getProvinceById(
            Long id) {

        Province province =
                entityManager.find(
                        Province.class,
                        id
                );

        if (province != null) {

            logger.info(
                    "Province retrieved: ID = {}, code = {}, name = {}",
                    province.getId(),
                    province.getCode(),
                    province.getName()
            );

        } else {

            logger.warn(
                    "No province found with id {}",
                    id
            );
        }

        return province;
    }

    @Override
    public void updateProvince(
            Province province) {

        entityManager.merge(province);

        logger.info(
                "Updated province with id {}",
                province.getId()
        );
    }

    @Override
    public void deleteProvince(
            Long id) {

        Province province =
                entityManager.find(
                        Province.class,
                        id
                );

        if (province != null) {

            entityManager.remove(province);

            logger.info(
                    "Deleted province with id {}",
                    id
            );
        }
    }

    @Override
    public boolean existsProvinceByCode(
            String code) {

        String query = """
                SELECT COUNT(p)
                FROM Province p
                WHERE UPPER(p.code) = :code
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

    @Override
    public boolean existsProvinceByCodeAndNotId(
            String code,
            Long id) {

        String query = """
                SELECT COUNT(p)
                FROM Province p
                WHERE UPPER(p.code) = :code
                  AND p.id != :id
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
    @Override
    public List<Province> listProvincesPage(
            int page,
            int size,
            String sortField,
            String sortDir) {

        int offset = page * size;

        CriteriaBuilder cb =
                entityManager.getCriteriaBuilder();

        CriteriaQuery<Province> cq =
                cb.createQuery(Province.class);

        Root<Province> root =
                cq.from(Province.class);

        root.fetch("region");

        String validSortField;

        switch (sortField) {

            case "id":
                validSortField = "id";
                break;

            case "code":
                validSortField = "code";
                break;

            case "name":
                validSortField = "name";
                break;

            default:
                validSortField = "id";
                break;
        }

        if ("desc".equalsIgnoreCase(sortDir)) {

            cq.orderBy(
                    cb.desc(
                            root.get(validSortField)
                    )
            );

        } else {

            cq.orderBy(
                    cb.asc(
                            root.get(validSortField)
                    )
            );
        }

        return entityManager
                .createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }


    @Override
    public Long countProvinces() {

        String query = """
            SELECT COUNT(p)
            FROM Province p
            """;

        return entityManager
                .createQuery(query, Long.class)
                .getSingleResult();
    }
}