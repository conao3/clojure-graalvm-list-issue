# clojure-graalvm-list-issue

# Solution

![](https://github.com/user-attachments/assets/20c917d7-a6b4-407e-b3d0-ac3ee4440a94)

(in Clojurians Slack)

# Issue

```clojure
(defn- home-path []
  (System/getProperty "user.home"))

(defn- local-dir []
  (io/file (home-path) ".local"))

(defn -main [& _args]
  (println (seq (.list (io/file (home-path) ".local"))))
  (println (seq (.list (local-dir)))))
```

The first `println` uses calling `io/file` directory, while the second `println` uses internal function.
Logically, both should have the same result, but when the binary compiled with graalvm is executed, the second example fails with the following error.

```
$ make build.native

$ clj -M -m graalvm-list-issue.main
(bin opt lib work state poetry python include pipx npm libexec tools share head)
(bin opt lib work state poetry python include pipx npm libexec tools share head)

$ java -jar target/graalvm-list-issue-standalone.jar
(bin opt lib work state poetry python include pipx npm libexec tools share head)
(bin opt lib work state poetry python include pipx npm libexec tools share head)

$ ./target/graalvm-list-issue
(bin opt lib work state poetry python include pipx npm libexec tools share head)
Exception in thread "main" java.lang.IllegalArgumentException: No matching field found: list for class java.io.File
	at clojure.lang.Reflector.getInstanceField(Reflector.java:414)
	at clojure.lang.Reflector.invokeNoArgInstanceMember(Reflector.java:457)
	at graalvm_list_issue.main$_main.invokeStatic(main.clj:14)
	at graalvm_list_issue.main$_main.doInvoke(main.clj:12)
	at clojure.lang.RestFn.invoke(RestFn.java:397)
	at clojure.lang.AFn.applyToHelper(AFn.java:152)
	at clojure.lang.RestFn.applyTo(RestFn.java:132)
	at graalvm_list_issue.main.main(Unknown Source)
	at java.base@22.0.1/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
```

# Environment

```shell
$ java -version
openjdk version "22.0.1" 2024-04-16
OpenJDK Runtime Environment GraalVM CE 22.0.1+8.1 (build 22.0.1+8-jvmci-b01)
OpenJDK 64-Bit Server VM GraalVM CE 22.0.1+8.1 (build 22.0.1+8-jvmci-b01, mixed mode, sharing)

$ native-image --version
native-image 22.0.1 2024-04-16
GraalVM Runtime Environment GraalVM CE 22.0.1+8.1 (build 22.0.1+8-jvmci-b01)
Substrate VM GraalVM CE 22.0.1+8.1 (build 22.0.1+8, serial gc)
```
