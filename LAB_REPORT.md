# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/nannalinlee-beep/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | Eclipse Temurin OpenJDK 21.0.12.1 (LTS) |
| Maven | Apache Maven 3.10.0 |
| IDE | Visual Studio Code 1.140.0 with Extension Pack for Java 0.31.2 |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | Claude Code in the Claude desktop app, model Claude Opus 5.5 (`claude-opus-5-5`) |

Note on how the SonarQube issues were collected: all SonarQube issues in this report come from the analyzer inside SonarQube for IDE 6.2.0, with its default rules. The AI tool started the extension's language server (`sonarlint-ls.jar` with `sonarjava.jar`) from a small script, opened every Java file of the project in it, and copied the issues it sent back. These are the same issues that the Problems panel shows with the source sonarqube. Warnings from the Java extension, such as the Scanner that is never closed, are left out.

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 19d7833

What I saw when I ran `store P1 6501 2`, `pickup P1 5` and `staff` on the starter code:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 0.0
P1 6501 STORED
Free lockers: 2
```

Three things looked wrong. The fee was 0.0, but a medium parcel kept for 5 days should cost (5 - 3) x 15 = 30 baht. After the pickup, the parcel was still STORED and the locker was still taken. The file parcels.csv was empty.

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 2 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 3 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 4 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |
| 5 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 6 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 7 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 10 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 11 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 12 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 13 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 25 October 2026 |
| AI tool and exact model | Claude Code in the Claude desktop app, model Claude Opus 5.5 (`claude-opus-5-5`) |
| Commit reviewed | 19d7833 |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | Opened the folder: the AI read the four Java files from parcelpoint/src/main/java/parcelpoint |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | String codes compared with == | both | java:S4973, probable bug | yes, because pickup returned Fee 0.0 and the parcel stayed STORED |
| 2 | ParcelService.java:76 | FileWriter never closed | both | java:S2095, resource not closed | yes, because parcels.csv stayed empty |
| 3 | ParcelService.java:80 | Empty catch block | both | java:S108, empty block | yes, because a failed save would give no message at all |
| 4 | ParcelService.java:6 | Unused import java.util.Date | both | java:S1128, unused import | yes, because nothing in the file uses Date |
| 5 | ParcelService.java:12 | Unused field maxParcels | both | java:S1068, unused field | yes, because nothing reads maxParcels |
| 6 | ParcelService.java:1 | Use the java.time API | SonarQube | java:S2143 | yes, but only because of the unused Date import; it went away when the import was removed |
| 7 | Main.java:9, 15, 17, 22 and ParcelService.java:30, 67, 69 | System.out used in place of a logger | SonarQube | java:S106 | partly: in ParcelService yes, because a service should not print; in Main no, because printing is the output of a command-line program |
| 8 | Main.java:8 | Scanner never closed | AI | resource not closed | yes, but it matters little, because the program ends right after the loop |
| 9 | Main.java:11-17 | Words of a command read without checking the length | AI | probable bug | yes, because `store P1` crashed with ArrayIndexOutOfBoundsException |
| 10 | ParcelService.java:52-62 | Magic numbers 3, 10, 15, 20 in the fee | AI | code smell | yes, because the free days and rates have no names |
| 11 | ParcelService.java:39-50 | pickUp returns 0 when the parcel is not found | AI | code smell | yes, because `pickup ZZ 9` printed Fee: 0.0, the same as a free pickup |
| 12 | ParcelService.java:9-83 | ParcelService does five jobs: lockers, fees, console, SMS and file | AI | weak class cohesion | yes, because it mixes rules with input/output |
| 13 | ParcelService.java:64-72 | printParcels(boolean forStaff) does two jobs picked by a flag | AI | logical cohesion | yes, because the flag chooses between two different lists |
| 14 | Main.java:19, 21 | Main passes the flag true or false to printParcels | AI | control coupling | yes, because the flag controls what the other method does |
| 15 | ParcelService.java:24-37 | storeParcel finds a locker, prints an SMS and saves the file | AI | procedural cohesion | yes, because printing the SMS is not part of storing a parcel |
| 16 | Parcel.java:7 | Field status is public and ParcelService writes it | AI | content coupling, field not private | yes, because ParcelService.java:28 and 44 change it directly |
| 17 | Parcel.java:13, ParcelService.java:28, 44, 68 | Status strings "NEW", "STORED", "PICKED_UP" must match in two classes | AI | connascence of meaning (convention) | yes, because a typing mistake in one class would still compile |
| 18 | Parcel.java:6, Locker.java:5, ParcelService.java:26, 56-58, Main.java:13 | Size is an int code 1, 2, 3 that four classes must read the same way | AI | connascence of meaning (convention) | yes, because `>=` at line 26 also depends on the order of the codes |
| 19 | ParcelService.java:20-22, Main.java:22 | getLockers returns the internal list, and Main reaches through it to count free lockers | AI | returns internal collection, strong coupling | yes, because Main could add or remove lockers |
| 20 | ParcelService.java:52 | fee gets the whole Parcel but only uses its size | AI | stamp coupling | yes, because only getSize() is called |
| 21 | Main.java:13 | Two String arguments of Parcel must stay in the right order | AI | connascence of position | yes, but it is a small risk with only two Strings |
| 22 | Main.java:6-27 | main chooses one of five commands by the first word | AI | logical cohesion | no, too strong: choosing a command is the normal single job of a menu loop |

2.1 Name two problems that only the AI review found.

The public field `status` in Parcel.java:7, which ParcelService changes directly, is content coupling. The size code 1, 2, 3 that Parcel, Locker, ParcelService and Main must all read the same way is connascence of meaning. SonarQube did not report either of them.

2.2 Name one problem that only SonarQube found, or write none.

SonarQube found java:S2143 at ParcelService.java:1, "Use the java.time API for date and time". The AI only reported that the Date import was unused. It also flagged every System.out (java:S106), which the AI did not report.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

Finding 20 in the AI review, which is No. 22 above, says that `main` has logical cohesion. I think this is too strong. A command-line menu must read a command and pick what to do, and that is the one job of `main`. I checked the other findings by opening each file at the line the AI gave, and by running the program with inputs such as `store P1` and `pickup ZZ 9` to see the bug happen.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Kept | Main is a command-line program, so printing with System.out is its output to the user. |
| 2 | Kept | Same reason as No. 1: this is the output of the program. |
| 3 | Kept | Same reason as No. 1: this is the output of the program. |
| 4 | Kept | Same reason as No. 1: this is the output of the program. |
| 5 | Fixed | d21a9a5 (removing the unused Date import also removed this issue) |
| 6 | Fixed | d21a9a5 |
| 7 | Fixed | ad87359 |
| 8 | Fixed | 0c8c781 (the SMS message moved from ParcelService to Main) |
| 9 | Fixed | a45e545 |
| 10 | Fixed | 0c8c781 (printParcels was removed from ParcelService) |
| 11 | Fixed | 0c8c781 (printParcels was removed from ParcelService) |
| 12 | Fixed | 05d5d61 |
| 13 | Fixed | 41ca84c |

Number of SonarQube issues that remain after your fixes: 8, all java:S106 in Main.java (lines 9, 15, 30, 32, 34, 40, 46, 48). I kept them because they are the output of the command-line program. There are more than 4 now because the printing moved from ParcelService into Main. My first design fix also made `main` too complex (java:S3776, Cognitive Complexity 17), so I fixed that in commit d7b03f7 by giving each command its own method.

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Weak class cohesion of ParcelService, and control coupling through printParcels(boolean forStaff) | Moved all console output from ParcelService to Main. Replaced printParcels(boolean) with two methods, getAllParcels() and getWaitingParcels(). Replaced getLockers(), which leaked the internal list, with countFreeLockers(). | 0c8c781 |
| Connascence of meaning: the int size code 1, 2, 3 used by Parcel, Locker, ParcelService and Main | Added the enum ParcelSize (SMALL, MEDIUM, LARGE), which holds the daily fee and a method fitsIn(). The fee now uses a named constant FREE_DAYS and the enum's daily fee. | d1f5be9 |

I also made one more small fix: `status` in Parcel is now private, and it uses a ParcelStatus enum. This removes the content coupling (Table 2, No. 16) and the status strings (No. 17). Commit 5adaa77.

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 25 October 2026 | 25 October 2026 |
| AI tool and exact model | Claude Code, Claude Opus 5.5 (`claude-opus-5-5`) | Claude Code, Claude Opus 5.5 (`claude-opus-5-5`) |
| Prompt (B or C as given, or your changed prompt in full) | Prompt B as given, with the BikeShare specification | Prompt C as given, with the BikeShare specification |
| Builds with mvn package (yes or no) | yes, on the first try | yes, on the first try |
| Number of classes, interfaces, enums and records | 1 class (Main) | 5 classes (Main, CommandLine, BikeShare, Bike, CsvRideStore), 1 interface (RideStore), 2 enums (MemberType, Station), 2 records (Ride, and Rental inside BikeShare) |
| SonarQube issues, total | 14 | 2 |
| SonarQube issues, by rule | java:S106 x 8, java:S2386 x 5, java:S3776 x 1 | java:S106 x 1, java:S1170 x 1 |
| AI review findings in Check 1 | 4 | 2 |
| AI review findings in Check 2 | 10 | 3 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

Note: the same AI session wrote both programs and then reviewed them. The reviews were not run in a new chat.

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

No. SonarQube did not report cohesion or coupling. The closest issues were java:S2386, "Make this member "protected"", on the public static lists at Main.java:14-18, and java:S3776, "Refactor this method to reduce its Cognitive Complexity from 18 to the 15 allowed", at Main.java:43. These are about access and complexity. SonarQube did not say that one class does every job, or that the boolean flag is control coupling.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. The poor version got 14 findings and the good version got 5, most of them only notes. One finding for bikeshare-poor is control coupling: `main` passes the flag `rent` to `handle(String[] w, boolean rent)` at Main.java:35 and 43. Nothing like this appears in bikeshare-good.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

I would extend bikeshare-good. I only need to add one line, `VISITOR(4)`, to the MemberType enum, and the fee, the input and the CSV file all work. In bikeshare-poor, I would need a new int code, and I would have to change the checks at Main.java:50 and 67. Anyone who reads rides.csv would also need to learn the new code.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

SonarQube checks the code line by line for bugs and code smells. For example, it found the == string comparison, the FileWriter that was never closed and the unused field maxParcels. It does not check design. It did not see that ParcelService did five jobs, that the int size code was shared by four classes, or that bikeshare-poor put everything in one class. Because of this, bikeshare-poor and bikeshare-good looked much closer in SonarQube than they really are.

5.2 When should you not trust the AI review? Give an example from this lab.

You should not trust the AI review when it gives a course term without a clear reason, or when it reviews its own code. In this lab the AI said that `main` in ParcelPoint has logical cohesion, which is too strong for a normal menu loop. The same AI also wrote the BikeShare code and then reviewed it, so it could be too kind to its own good design. I checked each finding by opening the file at the line it gave and by running the program.

5.3 What will you do differently in Project Phase 2 after this lab?

I will run SonarQube for IDE while I write code, so I can fix small problems like unused fields and unclosed files early. I will also run the AI review to check cohesion and coupling, because SonarQube cannot do that. I will use enums in place of number codes, and I will keep printing out of the classes that hold the rules.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Claude Code in the Claude desktop app, Claude Opus 5.5 (`claude-opus-5-5`) | Ran Prompt A on all three projects, which the lab asks for | reviews/parcelpoint-review.md, reviews/bikeshare-poor-review.md, reviews/bikeshare-good-review.md | Opened each file at the line given and ran the program to see the bugs (see Table 2) |
| Claude Code in the Claude desktop app, Claude Opus 5.5 (`claude-opus-5-5`) | Ran Prompt B and Prompt C, which the lab asks for | bikeshare-poor/ and bikeshare-good/ (all files) | Built both with mvn package and tried rentals and returns for students and staff |
| Claude Code in the Claude desktop app, Claude Opus 5.5 (`claude-opus-5-5`) | Collected the SonarQube issues, and made the fixes in parcelpoint | parcelpoint/src/main/java/parcelpoint (all fix commits after 19d7833) | Ran mvn package and the program after each fix, and ran SonarQube again at the end |
| Claude Code in the Claude desktop app, Claude Opus 5.5 (`claude-opus-5-5`) | Wrote the text of this report, including the answers to 2.1 to 5.3 | LAB_REPORT.md | Read the report and compared it with the code, the reviews and the program output |
