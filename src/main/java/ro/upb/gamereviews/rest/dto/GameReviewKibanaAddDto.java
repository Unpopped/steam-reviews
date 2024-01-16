package org.upb.gamereviews.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameReviewKibanaAddDto {
	int app_id;
	String app_name;
	int review_score;
	String review_text;
	int review_votes;
}
