# clojure-graalvm-list-issue

A minimal reproduction case demonstrating a reflection issue when using GraalVM native-image with Clojure's `java.io.File` interop.

## Problem

When calling `.list` on a `java.io.File` object returned from an internal function, GraalVM native-image fails with a reflection error, even though the same call works fine when the `File` object is created inline.

```clojure
(defn- home-path []
  (System/getProperty "user.home"))

(defn- local-dir []
  (io/file (home-path) ".local"))

(defn -main [& _args]
  (println (seq (.list (io/file (home-path) ".local"))))  ; Works
  (println (seq (.list (local-dir)))))                     ; Fails in native-image
```

Both calls are logically equivalent, but the native binary throws:

```
Exception in thread "main" java.lang.IllegalArgumentException: No matching field found: list for class java.io.File
```

## Reproducing the Issue

```shell
# Build native binary
make build.native

# JVM execution works fine
clj -M -m graalvm-list-issue.main

# JAR execution works fine
java -jar target/graalvm-list-issue-standalone.jar

# Native binary fails on the second println
./target/graalvm-list-issue
```

## Solution

The fix was discussed in the Clojurians Slack community:

![Solution from Clojurians Slack](https://github.com/user-attachments/assets/20c917d7-a6b4-407e-b3d0-ac3ee4440a94)

## Environment

Tested with:

- GraalVM CE 22.0.1
- Clojure 1.11.3
- native-image 22.0.1

## License

See [LICENSE](LICENSE) for details.
