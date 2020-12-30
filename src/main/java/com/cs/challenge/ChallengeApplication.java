package com.cs.challenge;

import com.cs.challenge.model.Record;
import com.cs.challenge.persistence.RecordEntity;
import com.cs.challenge.persistence.RecordRepository;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@SpringBootApplication
@EnableJpaRepositories("com.cs.challenge.persistence")
public class ChallengeApplication implements CommandLineRunner {

	final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private RecordRepository repo;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		validateInput(args.length);
		saveDurations(args[0]);
	}

	private void validateInput(int a) throws IOException {
		if (a < 1) {
			LOGGER.error("File path must be provided!");
			throw new IOException("File path must be provided!");
		}
		if (a > 1) {
			LOGGER.error("Too many arguments!");
			throw new IOException("Too many arguments!");
		}
	}

	private void saveDurations(String filePath) throws IOException {
		Gson gson = new Gson();
		try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
			reader.setLenient(true);
			Map<String, Long> buffer = new TreeMap<>();
			while (reader.peek() != JsonToken.END_DOCUMENT) {
				Record record = gson.fromJson(reader, Record.class);
				LOGGER.debug("Record parsed from file: " + record.toString());
				String id = record.getId();
				Long timestamp = buffer.get(id);
				if (Objects.isNull(timestamp)) {
					LOGGER.debug("Pair for id:" + id + "not found, storing");
					buffer.put(id, record.getTimestamp());
				} else {
					LOGGER.debug("Pair for id:" + id + "found, processing");
					buffer.remove(id);
					RecordEntity entity = new RecordEntity();
					entity.setId(id);
					long duration = Math.abs(record.getTimestamp() - timestamp);
					LOGGER.debug("Duration for id:" + id + "is calculated: " + duration);
					entity.setDuration(duration);
					if (duration > 4) {
						LOGGER.debug("Duration is more than 4ms, setting alert to true");
						entity.setAlert(true);
					}
					entity.setType(record.getType());
					entity.setHost(record.getHost());
					LOGGER.info("Saving duration: " + entity.toString());
					repo.save(entity);
				}
			}
		} catch (FileNotFoundException e) {
			LOGGER.error("File not found!: ", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}

	}

}
