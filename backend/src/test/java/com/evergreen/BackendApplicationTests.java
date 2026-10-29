package com.evergreen;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.evergreen.model.TrackedRepository;
import com.evergreen.service.TrackedRepositoryService;

import jakarta.transaction.Transactional;


@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {}

	@Autowired 
	TrackedRepositoryService service;

	@Test
	@Transactional
	void createAndReadTrackedRepository() {
		// write
		String owner = "test";
		String name = "test";
		UUID id = service.addTrackedRepository(owner, name).getId();

		// read
		Optional<TrackedRepository> result = service.getByID(id);
		assertThat(result).isPresent();

		// assert
		TrackedRepository saved = result.orElseThrow();
		assertThat(owner).isEqualTo(saved.getOwner());
		assertThat(name).isEqualTo(saved.getName());
	}
}
