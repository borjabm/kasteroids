# Solution

Initial code was ugly on multiple levels 😅, so my approach was to introduce minimal changes to satisfy requirements first.
Then applied more refactoring/kotlinization at the end (to answer point 2.).
Still a lot to do (hope what I got is enough here).

My steps reflected in git history.

1. App runs and returns output (no `ObjectMapper` issue), but tests don't pass.
   To make tests passing I needed to:
   - overcome [this](https://stackoverflow.com/questions/69267710/septembers-short-form-sep-no-longer-parses-in-java-17-in-en-gb-locale/) issue (added `locale = "US"`).
   - prefix test resource names with `/`.
2. [This done at the end, so that next step with `ObjectMapper` is possible (I took switch to `ktor` as part of "following Kotlin conventions").]
3. The issue is that `ObjectMapper` configuration is introduced, but other instances are used, not the one with altered config.
   Two ways of fixing that:
   1. Keep `@JsonIgnoreProperties`, abandon `ObjectMapper` config alteration:
      1. pros:
         - no need to find/alter all `ObjectMapper` instances.
         - per-entity config possible (i.e. tomorrow we may want to fail on some entities, but on some others not).
      2. cons:
         - need to repeat `@JsonIgnoreProperties` on all entities.
         - not what the original developer aimed for.
   2. Get rid of `@JsonIgnoreProperties` and rely on default `ObjectMapper` config.
      1. pros:
         - no need to repeat `@JsonIgnoreProperties`
      2. cons:
         - not suitable if for some entities we want to fail.
         - potentially need to repeat on each `ObjectMapper` instance created.
  
   I implemented latter option by getting rid of `@JsonIgnoreProperties` and using configured `ObjectMapper` instance.
   Then upgraded this by creating `ObjectMapper` derivative (`NasaObjectMapper`) with custom configuration in it (so no need to repeat if more instances is needed).
4. Need to add adequate filtering in `VicinityComparator` (and adjusted tests accordingly).
5. A few changes here:
   1. Introduced parallel fetch by using coroutines.
   2. Switched to `ktor` client and parser (non-blocking).
   3. Extra: extracted client to a separate class.

Finally, I got back to step 2. and "kotlinized"/polished the app (last code changing commit) :)
- Replaced java collections with kotlin analogues.
- Replaced java stream operation with kotlin analogues.
- Eliminated null in collection fields (defaulting to empty collections)


# ToDo
- What to do on invalid data, i.e. missing fields?
- Move some network/data error handling to client (return entities rather than `HttpResponse` from there).
- Proper testing
  - here we have multiple cases merged in one,
  - mocking (i.e. client),
  - missing coverage
- ...
