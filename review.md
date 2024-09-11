1. Review the code. What works and what doesn't?
   When running the app/tests I've run on following issues:
    * date deserialization error in `CloseApproachData.kt`

         ```
         com.fasterxml.jackson.databind.exc.InvalidFormatException: Can not deserialize value of type java.util.Date from String "2020-Jan-01 02:06": expected format "yyyy-MMM-dd hh:mm"
         ```
      Solution: specify locale in @JsonFormat. Also - I would suggest to use `java.time.*` API or `kotlinx-datetime` as
      java.util.Date is outdated.
