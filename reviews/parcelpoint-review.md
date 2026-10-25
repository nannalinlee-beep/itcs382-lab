# AI review of parcelpoint (Prompt A)

- Date: 25 October 2026
- AI tool and model: Claude Code in the Claude desktop app, model Claude Opus 5.5 (`claude-opus-5-5`)
- Commit reviewed: 19d7833 (Starter code)
- How the AI read the files: it opened the four Java files in the folder parcelpoint/src/main/java/parcelpoint
- Prompt: Prompt A from prompts.md, with `<folder>` replaced by `parcelpoint`

---

1. Files read: parcelpoint/src/main/java/parcelpoint/Locker.java, Main.java, Parcel.java, ParcelService.java

2. Findings

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | ParcelService.java:42 | `parcel.getCode() == code` compares two String objects by reference, so a code typed by the user never matches and pickup never finds the parcel. | Must fix | Use `parcel.getCode().equals(code)`. |
| 2 | 1 | ParcelService.java:76 | The `FileWriter` is never closed or flushed, so nothing is written and parcels.csv stays empty. | Must fix | Open the writer in a try-with-resources statement. |
| 3 | 1 | ParcelService.java:80-81 | The catch block is empty, so a failed save is silently ignored. | Must fix | Report the error or rethrow it as an unchecked exception. |
| 4 | 1 | ParcelService.java:6 | The import `java.util.Date` is unused. | Should fix | Remove the import. |
| 5 | 1 | ParcelService.java:12 | The private field `maxParcels` is never read. | Should fix | Remove the field, or use it to limit the number of parcels. |
| 6 | 1 | Main.java:8 | The `Scanner` on System.in is never closed. | Note | Open it in a try-with-resources statement. |
| 7 | 1 | Main.java:11-17 | The command line is split and `words[1]`, `words[2]` and `words[3]` are read without checking the length, and `Integer.parseInt` is not guarded, so a short or wrong command crashes the program. | Should fix | Check the number of words and catch `NumberFormatException` before using them. |
| 8 | 1 | ParcelService.java:52-62 | The fee uses the magic numbers 3, 10.0, 15.0 and 20.0. | Should fix | Give the free days and the daily rates names, for example in a size enum. |
| 9 | 1 | ParcelService.java:39-50 | `pickUp` returns 0 both when the parcel is not found and when the fee is free, so the caller cannot tell the two cases apart. | Should fix | Return an empty Optional or throw an exception when the code is not found. |
| 10 | 2 | ParcelService.java:9-83 | Weak class cohesion: `ParcelService` keeps the lockers, applies the fee rules, prints to the console, sends the SMS message and writes the CSV file, which are five different jobs. | Should fix | Move the console output to Main and the file saving to its own class. |
| 11 | 2 | ParcelService.java:64-72 | `printParcels(boolean forStaff)` has logical cohesion: the flag picks one of two different jobs inside the method. | Should fix | Split it into two methods, one for staff and one for students. |
| 12 | 2 | Main.java:19, Main.java:21 to ParcelService.java:64 | Control coupling: Main passes the flag `false` or `true` that decides what `printParcels` does. | Should fix | Call two separate methods instead of passing a flag. |
| 13 | 2 | ParcelService.java:24-37 | `storeParcel` has procedural cohesion: it finds a locker, changes the status, prints an SMS and saves the file, which are steps of one procedure that do not all work on the same data. | Should fix | Keep the locker search here and let the caller print the message. |
| 14 | 2 | Parcel.java:7, ParcelService.java:28, ParcelService.java:44, ParcelService.java:67-68 | The field `status` is public, and ParcelService writes it directly, which is content coupling. | Must fix | Make `status` private and change it through methods of Parcel. |
| 15 | 2 | Parcel.java:13, ParcelService.java:28, ParcelService.java:44, ParcelService.java:68 | Connascence of meaning (convention): both classes must spell the status strings "NEW", "STORED" and "PICKED_UP" the same way. | Should fix | Use an enum for the parcel status. |
| 16 | 2 | Parcel.java:6, Locker.java:5, ParcelService.java:26, ParcelService.java:56-58, Main.java:13 | Connascence of meaning (convention): the size is an int code where 1 means small, 2 medium and 3 large, and Parcel, Locker, ParcelService and Main must all read it the same way. `>=` at line 26 also depends on the order of the codes. | Should fix | Replace the int code with a `ParcelSize` enum. |
| 17 | 2 | ParcelService.java:20-22, Main.java:22 | `getLockers()` returns the internal list, so Main can add or remove lockers. Main also reaches through the service to the lockers to count the free ones. | Should fix | Return an unmodifiable list, or add a method `countFreeLockers()` to ParcelService. |
| 18 | 2 | ParcelService.java:52 | Stamp coupling: `fee(Parcel parcel, int days)` receives the whole Parcel but only uses its size. | Note | Pass only the size, or move the fee rule to the size type. |
| 19 | 2 | Main.java:13 | Connascence of position: `new Parcel(words[1], words[2], ...)` passes two String arguments whose order must match the constructor, and swapping them still compiles. | Note | Name the values in local variables before calling the constructor. |
| 20 | 2 | Main.java:6-27 | `main` has logical cohesion: one method reads input and chooses among five commands by comparing the first word. | Note | Move each command into its own small method. |

Not found: methods that are too long to read (the longest is `main`, 22 lines); string literals repeated many times (each status literal appears at most twice); connascence of algorithm between classes, because parcels.csv is only written and never read back.

3. Number of findings for each check

- Check 1, code quality: 9
- Check 2, cohesion, coupling and connascence: 11
