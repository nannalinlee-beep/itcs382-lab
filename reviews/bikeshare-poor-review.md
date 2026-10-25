# AI review of bikeshare-poor (Prompt A)

- Date: 25 October 2026
- AI tool and model: Claude Code in the Claude desktop app, model Claude Opus 5.5 (`claude-opus-5-5`)
- How the AI read the files: it opened the Java file in the folder bikeshare-poor/src/main/java/bikeshare
- Prompt: Prompt A from prompts.md, with `<folder>` replaced by `bikeshare-poor`. Run in the same Claude Code session that wrote the code, not in a new chat

---

1. Files read: bikeshare-poor/src/main/java/bikeshare/Main.java

2. Findings

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | Main.java:43-80 | `handle` is 38 lines long with nested if, for and try blocks, so it is hard to read. | Should fix | Split it into one method for renting and one for returning. |
| 2 | 1 | Main.java:67 | The fee rates 3 and 2 are magic numbers. | Should fix | Give each rate a name, for example in a member type enum. |
| 3 | 1 | Main.java:21-23 | The numbers 6 and 2 (`i / 2`) hide the rule "two bikes at each station". | Note | Use a named constant for the bikes per station. |
| 4 | 1 | Main.java:50 | Any member type other than "staff", even a typing mistake, is silently treated as a student. | Should fix | Reject a member type that is not "student" or "staff". |
| 5 | 2 | Main.java:43 | `handle(String[] w, boolean rent)` has logical cohesion: the flag picks one of two different jobs, renting or returning. | Must fix | Make two methods, `rent` and `returnBike`. |
| 6 | 2 | Main.java:35 to Main.java:43 | Control coupling: `main` passes the flag `rent` that decides what `handle` does. | Must fix | Call `rent` or `returnBike` directly. |
| 7 | 2 | Main.java:20-41 | `main` has temporal and procedural cohesion: it sets up the bikes and then runs the menu loop, which are unrelated steps done at start-up. | Should fix | Move the setup of the bikes into its own method or class. |
| 8 | 2 | Main.java:10-81 | Weak class cohesion: one class holds the menu, the bike records, the fee rule, the receipt and the file saving. | Must fix | Split it into classes such as Bike, a fee rule, a ride store and a menu. |
| 9 | 2 | Main.java:14-18 | The fields `stations`, `bikeIds`, `bikeStations`, `bikeMembers` and `bikeMemberTypes` are public, static and changeable, so any class could change them (common coupling). | Must fix | Make them private and put them inside a Bike class. |
| 10 | 2 | Main.java:15-18, Main.java:22-25, Main.java:51-54, Main.java:73-75 | Connascence of position: four parallel lists must keep the data of one bike at the same index, and every change must update all four in the same order. | Must fix | Use one list of Bike objects. |
| 11 | 2 | Main.java:11-12, Main.java:25, Main.java:50, Main.java:67, Main.java:75 | Connascence of meaning (convention): the member type is an int where 1 means student, 2 means staff and 0 means nobody. | Must fix | Use an enum for the member type. |
| 12 | 2 | Main.java:45, Main.java:50-53, Main.java:61, Main.java:66, Main.java:73 | Connascence of position: the method reads the command words by index (`w[1]`, `w[2]`, `w[3]`), and the meaning of `w[2]` and `w[3]` changes with the flag. | Should fix | Parse the words into named values before using them. |
| 13 | 2 | Main.java:70-71 | The CSV line stores the member type as the int code, so any program that reads rides.csv must know the same code (connascence of meaning across files). | Should fix | Write the type as a name such as STUDENT. |
| 14 | 2 | Main.java:69 | The file saving is inside the return logic, so the rule cannot be tested or changed without the file. | Should fix | Hide the saving behind an interface. |

Not found: unused imports, fields or parameters; resources that are not closed; empty blocks; objects compared with ==; content coupling between classes and reaching through one object to call another, because the program has only one class.

3. Number of findings for each check

- Check 1, code quality: 4
- Check 2, cohesion, coupling and connascence: 10
