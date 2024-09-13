1. Review the code. What works and what doesn't?
   When running the app/tests I've run on following issues:
   * date deserialization error in `CloseApproachData.kt`
     ```
     com.fasterxml.jackson.databind.exc.InvalidFormatException: Can not deserialize value of type java.util.Date from
     String "2020-Jan-01 02:06": expected format "yyyy-MMM-dd hh:mm"
     ```

     Solution: specify locale in @JsonFormat. Also - I would suggest to use `java.time.*` API or `kotlinx-datetime` as
     java.util.Date is outdated.
   * NPE in VicinityComparatorTest:
     ```
     java.lang.NullPointerException: Cannot invoke "java.net.URL.getProtocol()" because "url" is null
     ```
     Solution: add / to resource path

     Other review comments:
      * remove code from App - leave only main function there, move `checkForAsteroids()` to dedicated class
      * in tests move logic for creating neos to separate file and make it reusable for different tests
      * separate responsibilities in the code: HTTP client and API calls, printing results

2. As you may have noticed, this is a java project ported directly to Kotlin.
   1. Are there things that you would change to make the code more compliant with Kotlin coding conventions?
   2. And what about not importing any java libraries, would it be possible?
      * review nullability - now almost everything is nullable what contradicts kotlin's approach to nullability
      * replace Jackson mapper with Kotlin serialization `kotlinx.serialization`
      * replace java collections with kotlin collections (does all collections need to be mutable?)
      * remove java streams and use transformations that are available for kotlin collections
      * use kotlin dates instead java (especially remove java.util.Date)
      * use data classes for domain models
      * use kotlin HTTP client (Ktor client, khttp)
      * better Strings - maybe multiline?

3. There are some TODOs in the code: The original developer was frustrated that even if he configured object mapper to
   NOT fail on unknown properties, decoding did fail. He also had to add the annotation @JsonIgnoreProperties(
   ignoreUnknown = true) to have jacksons mapper accept unknown properties. Only one of these approaches should be
   enough. Can you find the reason?
   * Original developer defined 3 different object mappers and only one of them had defined config
     to `DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES`. We could refactor in three ways:
      * define one object mapper that could be injected where needed => one config for all objects
      * use `@JsonIgnoreProperties` on class level => fine-grained control
      * update existing mappers' config (including tests)

   I've created one mapper instance as an example.

   Can you do this in a more Kotlinistic way? Maybe without importing java libraries? I.e.: without importing jackson's
   libraries?
   * We could use `kotlinx.serialization` library
     https://kotlinlang.org/docs/serialization.html#example-json-serialization
4. The asteroids are supposedly sorted so only the 10 closest passings are shown. However, the data structure is such
   that each asteroid has a list of passings (CloseApproachDta), with time, velocity and distance from earth. The
   sorting is only judging distance, even if that passing occured a century ago. Can you rework the sort algorithm so
   that the 10 closest passings the coming week is shown? (see TODO in ApproachDetector, #61 and corresponding test
   class)
   * updated `VicinityComparator` to find the closest passings in the coming week by adding a filtering in date ranges

5. Once the list of asteroid IDs are found, the asteroid data is retrieved sequentially, which can take significant
   time. If all these secondary queries were performed in parallel the program would appear faster. Can you rework
   this? (if you don't have time to code this then prepare some design sketch and prepare for a discussion around
   parallel queries.)
   * added Ktor asynchronous HTTP client
   * defined HTTP client `NeoWsClient` in separate class
   * defined coroutines for fetching asteroids data

      

