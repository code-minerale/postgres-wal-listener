package com.fiko.cdc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiko.cdc.entity.Test;
import com.fiko.cdc.utils.Operation;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service interface that masks the caller from the implementation that fetches
 * / acts on test related data.
 *
 * @author Fiko
 */
@Service
public class TestService {

	/**
	 * Updates/Inserts tedst data.
	 *
	 * @param testData
	 * @param operation
	 */
	public void maintainReadModel(Map<String, Object> testData, Operation operation) {
		final ObjectMapper mapper = new ObjectMapper();
		final Test test = mapper.convertValue(testData, Test.class);
	}
}