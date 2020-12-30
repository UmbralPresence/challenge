package com.cs.challenge;

import com.cs.challenge.persistence.RecordEntity;
import com.cs.challenge.persistence.RecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(args = "src/test/resources/logfile.txt")
public class RecordRepositoryTest {

    @Autowired
    private RecordRepository repo;

    @Test
    public void testFindById() {
        repo.save(new RecordEntity("1", 3, null, null, false));
        assertThat(repo.findById("1"), instanceOf(Optional.class));
    }

    @Test
    public void testFindAll() {
        repo.save(new RecordEntity("1", 3, null, null, false));
        repo.save(new RecordEntity("2", 8, null, null, true));
        assertThat(repo.findAll(), instanceOf(List.class));
    }

    @Test
    public void testSave() {
        repo.save(new RecordEntity("1", 3, null, null, false));
        RecordEntity entity = repo.findById("1").orElseGet(() -> new RecordEntity("2", 8, null, null, true));
        assertEquals(3, entity.getDuration());
    }

}
