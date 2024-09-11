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

      

