package org.upb.gamereviews.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.upb.gamereviews.rest.dto.GameReviewAddDto;
import org.upb.gamereviews.rest.dto.GameReviewDto;
import org.upb.gamereviews.rest.dto.GameReviewKibanaAddDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	final RestTemplate restTemplate;

	static final String ELASTICSEARCH_URL_SEARCH = "http://localhost:9200/game_index/_search";
	static final String ELASTICSEARCH_URL_DOC = "http://localhost:9200/game_index/_doc";
	final ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping
	@SneakyThrows
	public ResponseEntity<List<GameReviewDto>> getReview(@RequestParam String reviewText, @RequestParam(defaultValue = "10") int size) {
		String searchQuery = reviewTextQuery(reviewText, size);
		String response = restTemplate.getForObject(ELASTICSEARCH_URL_SEARCH + "?q=" + searchQuery, String.class);

		JsonNode jsonNode = objectMapper.readTree(response);
		JsonNode hits = jsonNode.path("hits").path("hits");
		List<GameReviewDto> dtos = new ArrayList<>();
		for (JsonNode hit : hits) {
			GameReviewDto dto = objectMapper.treeToValue(hit.path("_source"), GameReviewDto.class);
			dto.setId(hit.path("_id").asText());
			dtos.add(dto);
		}

		return ResponseEntity.ok(dtos);
	}

	@GetMapping("/games/{appId}/reviews")
	public ResponseEntity<List<GameReviewDto>> getReviewsByAppId(@PathVariable int appId, @RequestParam(defaultValue = "10") int size) throws Exception {
		String searchQuery = appIdQuery(appId, size);
		String response = restTemplate.getForObject(ELASTICSEARCH_URL_SEARCH + "?q=" + searchQuery, String.class);

		JsonNode jsonNode = objectMapper.readTree(response);
		JsonNode hits = jsonNode.path("hits").path("hits");
		List<GameReviewDto> dtos = new ArrayList<>();
		for (JsonNode hit : hits) {
			GameReviewDto dto = objectMapper.treeToValue(hit.path("_source"), GameReviewDto.class);
			dto.setId(hit.path("_id").asText());
			dtos.add(dto);
		}

		return ResponseEntity.ok(dtos);
	}

	@PostMapping("/{appId}/reviews")
	public ResponseEntity<?> addReview(@PathVariable int appId, @RequestBody GameReviewAddDto reviewAddDto) {
		try {
			GameReviewKibanaAddDto review = new GameReviewKibanaAddDto();
			BeanUtils.copyProperties(reviewAddDto, review);
			review.setApp_id(appId);
			if (reviewAddDto.isReview_score()) {
				review.setReview_score(1);
			}
			else {
				review.setReview_score(0);
			}
			ResponseEntity<String> response = restTemplate.postForEntity(ELASTICSEARCH_URL_DOC, review, String.class);

			return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}


	private String reviewTextQuery(String appId, int size) {
		return "review_text:" + "\"" + appId + "\"" + "&size=" + size;
	}

	private String appIdQuery(int userQuery, int size) {
		return "app_id:" + "\"" + userQuery + "\"" + "&size=" + size;
	}
}
