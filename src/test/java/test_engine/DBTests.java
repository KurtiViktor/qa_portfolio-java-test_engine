package test_engine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test_engine.db.jpa.service.impl.JPAServiceImpl;
import test_engine.db.model.Provider;
import test_engine.db.model.Warehouse;
import test_engine.ext.junit5.interf.DB;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceProperty;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит-тесты для проверки db-тестов.
 */
@DB
@Slf4j
@DisplayName("Юнит-тесты для проверки db-тестов")
class DBTests {

    @PersistenceContext(
            unitName = "engine-test",
            properties = {
                    @PersistenceProperty(name = "javax.persistence.jdbc.url", value = "${jdbc.url}")
            })
    private EntityManager entityManager;

    /**
     * Тест поиска всех элементов в таблице.
     *
     * @param jpa JPA-сервисы, будут инъектированы
     */
    @Test
    void testFindAll(JPAServiceImpl jpa) {
        log.info("LOG TESTING WITH LOMBOK ");
        List<Provider> providers = jpa.findAll(entityManager, Provider.class, "Provider.findAll");
        assertEquals(providers.size(), 5, "FindAll check");
    }

    /**
     * Тест поиска по id в таблице.
     *
     * @param jpa JPA-сервисы, будут инъектированы
     */
    @Test
    void testFindById(JPAServiceImpl jpa) {
        Provider provider = jpa.findById(entityManager, Provider.class, 1L);
        assertAll("FindById check",
                () -> assertEquals(provider.getName(), "Provider 1"),
                () -> assertEquals(provider.getId(), 1)
        );
    }

    /**
     * Тест сохранения объекта в таблицу.
     *
     * @param jpa JPA-сервисы, будут инъектированы
     */
    @Test
    void testSave(JPAServiceImpl jpa) {
        Warehouse savedWarehouse = new Warehouse();
        savedWarehouse.setId(7L);
        savedWarehouse.setAddress("testName");
        jpa.save(entityManager, savedWarehouse);
        Warehouse providerFromDb = jpa.findById(entityManager, Warehouse.class, savedWarehouse.getId());
        assertEquals(savedWarehouse, providerFromDb);
        jpa.delete(entityManager, providerFromDb);
    }

    /**
     * Тест удаления объекта из таблицы.
     *
     * @param jpa JPA-сервисы, будут инъектированы
     */
    @Test
    void testDelete(JPAServiceImpl jpa) {
        Warehouse deletedWarehouse = new Warehouse();
        deletedWarehouse.setId(-7L);
        deletedWarehouse.setAddress("deleteTestName");
        jpa.delete(entityManager, deletedWarehouse);
        assertNull(
                jpa.findById(entityManager, Warehouse.class, deletedWarehouse.getId())
        );
    }

}
