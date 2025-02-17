## Existing code review & improvements made

- Application issued a warning that no logger implementation is available, 
because SLF4J is just an interface. I added `logback` dependency to mitigate it.
Moreover, there is no logger configured and all output goes to stdout, however,
for such a simple console application, it's perfectly reasonable
- JUnit 4 is used along with deprecated matchers - definitely worthwhile to upgrade
to its successor. I only bumped the version, because previously used one has
security issues
- No string interpolation is used
- Type Any is used even if it's possible to use more specific type
- I introduced an abstraction over Nasa's NEO API and over config to separate
responsibilities from the App class. Moreover, I switched to a JSON parser singleton
to deal with the assignment no. 2, so that there's only a single strategy of JSON 
parsing. Config abstraction plays nicely with the setup and allows to hide
the details regarding default option for API key and mitigates a possible problem
with mocking static `getnenv` method, e.g. in tests.
- MutableList is used across multiple classes for no reason. In general, it's preferable to 
use immutable types and implementations.
- I deleted all not used model classes

### Current design flaw regarding results presentation
There can be a case when more than one passing of a single asteroid should be on the final list. The current code
always takes only the closest passing of any given asteroid without comparing its other passings to other asteroids' passings.
I would in general implement it in such a way that ApproachDetector prepares the data to be directly
displayed by the App:
1. Create a new class with the display representation e.g. NEOPassing
2. flatMap all close approach dates of all neos keeping the reference to id, name, hazard, distance, date and create
a NEOPassing instances out of them
3. Sort them by "is in this week" and distance - can be done via Comparator
4. No extra sorting needed in the App

## Further improvements & Kotlinization
- Api class after my changes still doesn't have a single responsibility. With the current setup it
should rather only be responsible for orchestration and construction of the dependency tree. 
Would be good to create a class responsible for printing the results.
- Make data classes out of the classes in model package. Decouple API model from the domain
model and declare non-nullable properties in it.
- Wildcard imports may shadow names from other packages
- Improve error-handling, create designated exceptions per scenario
- Use Kotlin methods instead of Java's Stream API

