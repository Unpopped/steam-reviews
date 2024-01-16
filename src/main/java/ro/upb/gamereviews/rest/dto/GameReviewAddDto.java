package org.upb.gamereviews.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameReviewAddDto {
	String app_name;
	boolean review_score;
	String review_text;
	int review_votes;
}
