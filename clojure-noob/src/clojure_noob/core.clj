(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defmacro my-when
  [test & actions]
  `(if test
     (do ~@actions)))

(my-when true
         (print "HEY")
         (print " THERE"))

(defmacro my-or
  ([] nil)
  ([x] x)
  ([x & xs]
   `(if ~x
      ~x
      (my-or ~@xs))))

(my-or false false false)
(my-or true false false)
(my-or false false nil)
(my-or "hi" false false)
(my-or)

(def x {:logic inc})

((get x :logic) 4)

((x :unlogic dec) 6)

(:logic x)

(:logic nil)
