package com.bts.bugstalker.core.user;

import com.bts.bugstalker.fixtures.EntityMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    private void persistUsers() {
        assertThat(userRepository.count()).isEqualTo(0);

        var user1 = EntityMocks.USER.prepare();
        user1.setEmail("user1@email.com");
        user1.setUsername("username1");

        var user2 = EntityMocks.USER.prepare();
        user2.setEmail("user2@email.com");
        user2.setUsername("username2");

        var user3 = EntityMocks.USER.prepare();
        user3.setEmail("user3@email.com");
        user3.setUsername("username3");

        userRepository.saveAll(List.of(user1, user2, user3));
    }

    @Test
    void shouldSuccessfullyPersistUser() {
        long count = userRepository.count();
        var user = EntityMocks.USER.prepare();

        userRepository.save(user);

        assertThat(userRepository.count()).isEqualTo(count + 1);
    }

    @Test
    void shouldSuccessfullyUpdateEntityVersion() {
        Long id = userRepository.save(EntityMocks.USER.prepare()).getId();
        var initial = userRepository.findById(id).orElseThrow();
        assertThat(initial.getVersion()).isEqualTo(0);

        initial.setLastName("last name updated");
        userRepository.save(initial);

        var updated = userRepository.findAll();
        assertThat(updated).hasSize(1).allMatch(
                userEntity -> userEntity.getVersion() == 1
        );
    }

    @Test
    void shouldMapDataCorrectly() {
        var user = EntityMocks.USER.prepare();
        var persisted = userRepository.save(user);

        assertAll(
                () -> assertThat(persisted).isNotNull(),
                () -> assertThat(persisted.getId()).isNotNull(),
                () -> assertThat(persisted.getUsername()).isEqualTo(user.getUsername()),
                () -> assertThat(persisted.getFirstName()).isEqualTo(user.getFirstName()),
                () -> assertThat(persisted.getLastName()).isEqualTo(user.getLastName()),
                () -> assertThat(persisted.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(persisted.getRole()).isEqualTo(user.getRole()),
                () -> assertThat(persisted.getPassword()).isEqualTo(user.getPassword())
        );
    }

    @Test
    void shouldFindByEmail() {
        persistUsers();
        String email = "user1@email.com";

        var user = userRepository.findByEmail(email);

        assertAll(
                () -> assertTrue(user.isPresent()),
                () -> assertThat(user.orElseThrow().getEmail()).isEqualTo(email)
        );
    }

    @Test
    void shouldFindByUsername() {
        persistUsers();
        String username = "username1";

        var user = userRepository.findByUsername(username);

        assertAll(
                () -> assertTrue(user.isPresent()),
                () -> assertThat(user.orElseThrow().getUsername()).isEqualTo(username)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"USERNAME", "USER@EMAIL.COM", "FIRSTNAME", "LASTNAME", "LASTNAMEFIRSTNAME", "FIRSTNAMELASTNAME"})
    void shouldFindByQuery(String query) {
        var user = EntityMocks.USER.prepare();
        userRepository.save(user);

        var foundUsers = userRepository.searchByQuery(query);

        assertThat(foundUsers).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"username_1", "@email.pl", "name_1"})
    void shouldNotFindByQuery(String query) {
        var user = EntityMocks.USER.prepare();
        userRepository.save(user);

        var foundUsers = userRepository.searchByQuery(query);

        assertThat(foundUsers).hasSize(0);
    }
}
