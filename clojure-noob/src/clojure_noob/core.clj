(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defn makeAFunc [a]
  (println a))

(defn call-a-func []
  (makeAFunc "heeeeey"))

(call-a-func)
