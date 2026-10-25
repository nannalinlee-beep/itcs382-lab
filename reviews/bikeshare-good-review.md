# AI review of bikeshare-good (Prompt A)

- Date: 25 October 2026
- AI tool and model: Claude Code in the Claude desktop app, model Claude Opus 5.5 (`claude-opus-5-5`)
- How the AI read the files: it opened the nine Java files in the folder bikeshare-good/src/main/java/bikeshare
- Prompt: Prompt A from prompts.md, with `<folder>` replaced by `bikeshare-good`, in a new chat

---

1. Files read: bikeshare-good/src/main/java/bikeshare/Bike.java, BikeShare.java, CommandLine.java, CsvRideStore.java, Main.java, MemberType.java, Ride.java, RideStore.java, Station.java

2. Findings

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | CommandLine.java:8 | The field `out` always holds `System.out`, so it could be a constant or be passed in through the constructor. | Note | Pass the PrintStream into the constructor, which also makes the class easier to test. |
| 2 | 1 | BikeShare.java:45-49 | The loop keeps going after it finds the bike, and the check `bike.isRented()` repeats what `rentals` already says. | Note | Stop the loop when the bike is found. |
| 3 | 2 | BikeShare.java:13, Bike.java:6 | Two places record that a bike is rented: the map `rentals` and the field `rented` of Bike, so they must be kept in step (connascence of value). | Should fix | Keep the rental state in one place, for example a nullable Rental field in Bike. |
| 4 | 2 | CommandLine.java:40-41 | The receipt and messages print the station as an enum name such as LIBRARY, while the user types Library. | Note | Add a display name to Station. |
| 5 | 2 | CommandLine.java:32-33, MemberType.java:17-18, Station.java:8-9 | Connascence of name between what the user types and the enum constant names: renaming a constant changes the commands the user must type. | Note | Map the input text to the constants in one explicit table if the names may change. |

Not found: methods with procedural or weaker cohesion; classes that represent more than one thing; control, common or content coupling (each call passes only plain values, enums or the Ride record, which `CsvRideStore.save` uses in full); fields that are not private; operations that return an internal collection; unused code; resources that are not closed; empty blocks.

3. Number of findings for each check

- Check 1, code quality: 2
- Check 2, cohesion, coupling and connascence: 3
