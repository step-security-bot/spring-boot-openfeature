package org.iromu.openfeature.examples;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
/**
 * @author Ivan Rodriguez
 */
@RestController
@RequestMapping("/feature")
public class UnleashController {

	private final Client client;

	private final MockedUnleashApiServer mockedUnleashApiServer;

	public UnleashController(Client client, MockedUnleashApiServer mockedUnleashApiServer) {
		this.client = client;
		this.mockedUnleashApiServer = mockedUnleashApiServer;

		Boolean booleanValue = client.getBooleanValue("sample", false);
		System.out.println(booleanValue);
	}

	@GetMapping("toggle")
	public Boolean toggle(@RequestParam final String name) {
		mockedUnleashApiServer.toggle();
		return client.getBooleanValue(name, false);
	}

	@GetMapping("{name}")
	public Boolean feature(@PathVariable("name") final String name) {
		return client.getBooleanValue(name, false);
	}

	@GetMapping("user/{id}")
	public Boolean featureOnUserId(@PathVariable("id") final String id) {
		return client.getBooleanValue("users-flag", false, new ImmutableContext(Map.of("userId", new Value(id))));
	}

}
