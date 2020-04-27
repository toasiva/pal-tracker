package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    @Test
    public void create() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry createdTimeEntry = repo.create(TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));


        TimeEntry expected = TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        assertThat(createdTimeEntry).isEqualTo(expected);

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void find() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        repo.create(TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));


        TimeEntry expected = TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8);
        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void find_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long timeEntryId = 1L;

        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isNull();
    }

    @Test
    public void list() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        long timeEntryId = 3L;
        repo.create(TimeEntry.createTimeEntry(timeEntryId, 123L, 456L, LocalDate.parse("2017-01-08"), 8));
        timeEntryId = 4L;
        repo.create(TimeEntry.createTimeEntry(timeEntryId, 789L, 654L, LocalDate.parse("2017-01-07"), 4));

        List<TimeEntry> expected = asList(
                TimeEntry.createTimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
                TimeEntry.createTimeEntry(2L, 789L, 654L, LocalDate.parse("2017-01-07"), 4)
        );
        assertThat(repo.list()).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void update() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        long timeEntryId = 1L;
        TimeEntry created = repo.create(TimeEntry.createTimeEntry(timeEntryId, 123L, 456L, LocalDate.parse("2017-01-08"), 8));

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                TimeEntry.createTimeEntry(timeEntryId, 321L, 654L, LocalDate.parse("2017-01-09"), 5));

        TimeEntry expected = TimeEntry.createTimeEntry(created.getId(), 321L, 654L, LocalDate.parse("2017-01-09"), 5);
        assertThat(updatedEntry).isEqualTo(expected);
        assertThat(repo.find(created.getId())).isEqualTo(expected);
    }

    @Test
    public void update_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        long timeEntryId = 1L;
        TimeEntry updatedEntry = repo.update(timeEntryId, TimeEntry.createTimeEntry(timeEntryId, 321L, 654L, LocalDate.parse("2017-01-09"), 5));

        assertThat(updatedEntry).isNotNull();
    }

    @Test
    public void delete() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry created = repo.create(TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }

    @Test
    public void deleteKeepsTrackOfLatestIdProperly() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        long timeEntryId = 1L;
        TimeEntry created = repo.create(TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        assertThat(created.getId()).isEqualTo(1);

        repo.delete(created.getId());

        TimeEntry createdSecond = repo.create(TimeEntry.createTimeEntry(timeEntryId, projectId, userId, LocalDate.parse("2017-01-08"), 8));

        assertThat(createdSecond.getId()).isEqualTo(2);
    }
}
