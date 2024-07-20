(ns graalvm-list-issue.main
  (:require
   [clojure.java.io :as io])
  (:gen-class))

(defn- home-path []
  (System/getProperty "user.home"))

(defn- local-dir []
  (io/file (home-path) ".local"))

(defn -main [& _args]
  (println (seq (.list (io/file (home-path) ".local"))))
  (println (seq (.list (local-dir)))))
