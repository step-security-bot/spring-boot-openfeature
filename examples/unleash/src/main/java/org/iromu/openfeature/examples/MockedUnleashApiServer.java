package org.iromu.openfeature.examples;

import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.iromu.openfeature.boot.autoconfigure.unleash.UnleashProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Ivan Rodriguez
 */
@Component
@Slf4j
public class MockedUnleashApiServer {

	String features;

	public MockedUnleashApiServer(MockWebServer mockWebServer, UnleashProperties properties) throws IOException {
		features = new String(Files.readAllBytes(properties.getBackupFile().getFile().toPath()));
		mockWebServer.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) {
				String path = request.getPath();
				log.info("Requested {}", path);
				if ("/api/client/metrics".equals(path)) {
					return new MockResponse().setResponseCode(200);
				}
				else if ("/api/client/register".equals(path)) {
					return new MockResponse().setResponseCode(200);
				}
				else if ("/api/client/features".equals(path)) {
					return new MockResponse().setBody(features)
						.addHeader("Content-Type", "application/json")
						.setResponseCode(200);
				}
				else {
					return new MockResponse().setResponseCode(404);
				}
			}
		});

		log.info("MockedUnleashApiServer init.");
	}

	public void toggle() {

	}

}
