# Assignment
Your assignment is to 
1. Review the code. What works and what doesn't? 
2. As you may have noticed, this is a java project ported directly to Kotlin. 
   1. Are there things that you would change to make the code more compliant with Kotlin coding conventions?
   2. And what about not importing any java libraries, would it be possible?
3. There are some TODOs in the code: The original developer was frustrated that even if he configured object mapper to NOT fail on unknown properties, decoding did fail. He also had to add the annotation 
```@JsonIgnoreProperties(ignoreUnknown = true)``` to have jacksons mapper accept unknown properties. Only one of these approaches should be enough. Can you find the reason?
   1. Can you do this in a more Kotlinistic way? Maybe without importing java libraries? I.e.: without importing jackson's libraries?

4. The asteroids are supposedly sorted so only the 10 closest passings are shown. However, the data structure is such that each asteroid has a list of passings (CloseApproachData), with time, velocity and distance from earth. The sorting is only judging distance, even if that passing occured a century ago. Can you rework the sort algorithm so that the 10 closest passings the coming week is shown? (see TODO in ApproachDetector, #61 and corresponding test class)
5. Once the list of asteroid IDs are found, the asteroid data is retrieved sequentially, which can take significant time. If all these secondary queries were performed in parallel the program would appear faster. Can you rework this?
   (if you don't have time to code this then prepare some design sketch and prepare for a discussion around parallel queries.)  
