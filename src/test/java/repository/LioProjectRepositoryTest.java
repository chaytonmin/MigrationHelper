package repository;

import edu.pku.migrationhelper.config.MongoDbConfiguration;
import edu.pku.migrationhelper.data.lib.LioProject;
import edu.pku.migrationhelper.repository.LioProjectRepository;
import edu.pku.migrationhelper.service.MongoDbUtilService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootTest(classes = { MongoDbConfiguration.class, MongoDbUtilService.class })
public class LioProjectRepositoryTest {

    @Autowired
    MongoDbUtilService utilService;

    @Autowired
    LioProjectRepository lioProjectRepository;

    @Before
    public void init() {
        assertTrue(utilService.getDbName().contains("test"));
        lioProjectRepository.deleteAll();
        utilService.initMongoDb();
    }

    @Test
    public void testLioProjectRepository() {
        LioProject l = new LioProject();
        l.setName("abc");
        l.setSourceRank(1).setId(101);
        lioProjectRepository.save(l);
        assertTrue(lioProjectRepository.findByName("abc").isPresent());

        for (int i = 0; i < 100; ++i) {
            LioProject l2 = new LioProject().setSourceRank(i).setId(i);
            lioProjectRepository.save(l2);
        }

        Page<LioProject> result = lioProjectRepository.findAll(
                PageRequest.of(0, 10, Sort.by("sourceRank").ascending()));
        assertEquals(10, result.getSize());
        assertEquals(11, result.getTotalPages());
        assertEquals(0, result.getContent().get(0).getSourceRank());
    }
}