# Clarity AI coding assignment

## Running

### Prerequisites

Developed and tested on macOS, OpenJDK build 15+36. Should work on other *nixes and from Java 11 onwards.

### 1. Parse the data with a time_init, time_end

Execute the script `connected-source-hosts`, or alternatively `gradlew runConnectedSourceHosts --args="<arguments and options>"`.

Usage (`connected-source-hosts --help`):

```
Usage: connected-source-hosts [OPTIONS] LOG_FILE_NAME TARGET

Options:
  -f, --from INT  timestamp from inclusive [ms] (default: 0)
  -t, --to INT    timestamp to inclusive [ms] (default: 9223372036854775807)
  -h, --help      Show this message and exit

Arguments:
  LOG_FILE_NAME  log file name
  TARGET         search target of incoming connections
```

Default value for the `-t/--to` option is `Long.MAX_VALUE`

Example:

```
./connected-source-hosts input-file-10000.txt Aaliayh --from=1565656607767 --to=1565680778409  
```

### 2. Unlimited Input Parser

Execute the script `periodic-reports`, or alternatively `gradlew runPeriodicReports --args="<arguments and options>"`.

Usage (`periodic-reports --help`):

```
Usage: periodic-reports [OPTIONS] LOG_FILE_NAME HOST

Options:
  -f, --frequency INT  reporting frequency [ms] (default: 3600000)
  -l, --max-lag INT    maximum tolerable lag of log entries [ms] (default: 300000)
  -t, --timeout INT    log inactivity timeout [ms] (default: 30000)
  -h, --help           Show this message and exit

Arguments:
  LOG_FILE_NAME  log file name
  HOST           host to report both incoming and outgoing connections
```

Default value for the `-f/--frequency` option is 1h. Default value for the `-l/--max-lag` option is 5m

Example:

```
./periodic-reports input-file-10000.txt Ferid -f 3600000 -t 0  
```

## Assumptions

- Blank log lines are allowed. They are skipped during the processing
- Malformed log throw an exception and stop the processing
- `periodic-reports` script ("Unlimited Input Parser" part), `HOST` argument sets both the target host for searching connected source hosts
  and the source host for searching target hosts that received connections from that host
- If there are multiple source hosts that generated the maximum number of connections, `periodic-reports` script prints all the source hosts
  and the number of connections
- First line timestamp sets the boundary of reporting periods

## Design

The solution follows the Dependency Inversion principle (aka Hexagonal Architecture, Clean Architecture, you name it).

### Entry points

The main entry points are `main` functions in `ConnectedSourceHosts.kt` and `PeriodicReports.kt` files.

### Domain

Entry points interact with the _domain_ through _handlers_ (aka use case or application services): `ConnectedSourceHostsHandler` and
`PeriodicReportsHandler`. `ConnectedSourceHostsHandler` is rather simple and self-contained. `PeriodicReportsHandler` collaborates with
peers from the `periodicreports` package.

`log` package contains types used in both handlers - some basic type definitions and log reading abstraction (`LogReader`).

### Infrastructure

Infrastructure implementation of the _domain_ abstractions is implemented in `file` and `renderer` packages.

### Performance

Top connection counter has `n` space complexity and `n log(n)` time complexity.

To run the programs with a low-memory JVM, add this option to the `runConnectedSourceHosts` and `runPeriodicReports` tasks:

```
jvmArgs = listOf("-Xmx128M")
```

## Kotlin

### Inline classes

I used Kotlin [inline classes](https://kotlinlang.org/docs/inline-classes.html) to wrap primitive types and avoid thus _primitive obsession_
. They don't increase memory and execution overhead - Kotlin compiler replaces them with the wrapped type during the compilation. They are
still in beta in Kotlin 1.4 and will become stable in Kotlin 1.5 (under development). As explained
in [Components stability](https://kotlinlang.org/docs/components-stability.html), Kotlin beta features can be used in typical production
scenarios, although there can be breaking changes between Kotlin versions.

### Coroutines

Although the solution could be implemented using Java thread API, I decided to
use [Kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and `suspend` functions. Coroutine runtime and testing library
gives a higher level of abstraction over concurrency and decouples the execution policies (thread pools, single-threaded event loops) from
concurrency constructs.

As you can see in `FileLogReaderTest`, production code is tested with `TestCoroutineContext` which gives the precise control over timing. I
was able to verify the timeouts without executing `FileLogReader` with a thread pool. As a result, the tests are very fast (no sleeps)
and deterministic.

## Testing

I followed classical/Chicago TDD with micro- to small commits (check the history).

Unit tests are _social_ - they are testing handlers and their dependencies - `PeriodicReportGenerator` and `ReportCollector`, since only
the `PeriodicReportGenerator` has a non-trivial amount of branching logic. No mocking library was required.

Both use cases have their acceptance tests with the sample log file attached to the assignment.

### Generating huge log files

Run `LogGenerator.kt` to generate random logs with the given number of lines.
