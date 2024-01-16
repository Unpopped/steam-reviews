# steam-reviews

### Game Review Search and Recommendation Application ###

A web-based application that lets users search a database for game reviews and that gives them recommendations based on what similar users searched. This will help the users find games that match their preferences and interests, and to discover new games that they might enjoy.
   
### Technologies ###
 
* Spring boot framework
* Elastic search
* Docker
* React
* r4tings

### Endpoints ###

  * GET /search?query={query} : allows users to search for games by keywords, genres, themes, platforms, etc. The query parameter is a string that contains the user’s search terms. The endpoint returns a list of game reviews that match the query, along with their ratings, popularity, relevance, etc.
  
        Example request: GET /search?query=horror+games
        Example response: {"reviews": [{"id": "123", "title": "Resident Evil Village", "rating": 4.5, "popularity": 100, "relevance": 0.9}, {"id": "456", "title": "Outlast 2", "rating": 3.5, "popularity": 80, "relevance": 0.8}, ...]}
    
  * GET /review/{gameId} : allows users to view a game review by its id. The gameId parameter is a string that identifies the game. The endpoint returns the game review from different sources and platforms, and allows users to compare them based on various criteria.
  
        Example request: GET /review/123
        Example response: {"review": {"id": "123", "title": "Resident Evil Village", "rating": 4.5, "source": "Steam", "platform": "PC", "content": "A thrilling and immersive horror game that ..."}}
    
  * POST /rate/{gameId}?rating={rating} : allows users to rate a game by its id. The gameId parameter is a string that identifies the game. The rating parameter is an integer between 1 and 5 that represents the user’s rating. The endpoint updates the user’s rating for the game, and returns a confirmation message.
  
        Example request: POST /rate/125?rating=5
        Example response: {"message": "You have rated 'Cyberpunk 2077' 5 stars. Thank you for your feedback!"}
    
 * GET /recommend/{userId} : allows users to get recommendations for games that they might like, based on their search history and ratings. The userId parameter is a string that identifies the user. The endpoint returns a list of game recommendations for the user, along with the reasons why they were recommended.
  
        Example request: GET /recommend/abc
        Example response: {"recommendation": {"id": "789", "title": "The Evil Within 2", "rating": 4.0, "reason": "You searched for horror games and rated Resident Evil Village 5 stars. You might also like this game."}}
  
