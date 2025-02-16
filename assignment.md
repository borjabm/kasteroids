# Assignment

Your assignment is to

1. Review the code. What works and what doesn't?
2. There are some TODOs in the code: The original developer was frustrated that even if he configured Json parser to NOT
   fail on unknown properties, decoding did fail. He also had to add the annotation
   ```@JsonIgnoreUnknownKeys``` to have the parser accept unknown properties. Only one of these approaches should be
   enough. Can you find the reason?

3. The asteroids are supposedly sorted so only the 10 closest passings are shown. However, the data structure is such
   that each asteroid has a list of passings (CloseApproachData), with time, velocity and distance from earth. The
   sorting is only judging distance, even if that passing occurred a century ago. Can you rework the sort algorithm so
   that the 10 closest passings the coming week is shown? (see TODO in ApproachDetector, #69 and corresponding test
   class)
4. Once the list of asteroid IDs are found, the asteroid data is retrieved sequentially, which can take significant
   time. If all these secondary queries were performed in parallel the program would appear faster. Can you rework this?
   (if you don't have time to code this then prepare some design sketch and prepare for a discussion around parallel
   queries.)
5. Review the code in general. Are there things that you would change to make the code more compliant with Kotlin coding
   conventions?

---

# Solutions

1. Fixed problems with parsing json responses. There were several issues:
    1. Parsing was done manually using ```json.decodeFromString(.)```. HttpClient that is used allows to do that
       automatically - by setting json parser during HttpClient creation and using ```.body()``` method that returns
       already deserialized response.
    2. Manual parsing used json parser without ```ignoreUnknownKeys = true``` option, that required cumbersome
       annotations for model classes ```@OptIn(ExperimentalSerializationApi::class)``` and  ```@JsonIgnoreUnknownKeys```
       that solved the problem with ignoring properties from JSON response, that were not defined in models.
2. Used HttpClient for json deserialization and removed ```@JsonIgnoreUnknownKeys``` from models
3. Excluded asteroid passings that happened before current week
4. Reworked NASA API client to fetch asteroids in a concurrent manner, using concurrent batches of configurable size.
5. Multiple refactorings that improve logging, readability, maintainability, testability and adherence to Kotlin way of
   doing things.

# Proposed future improvements

1. Migrate to JUnit 5 and enhance unit tests with additional scenarios involving edge cases
2. Introduce integration tests
3. Introduce a lightweight DI framework (Google Guice?)
4. Implement error handling for NASA API client
5. Improve resilience by performing API request retries in case of throttles or temporary errors
