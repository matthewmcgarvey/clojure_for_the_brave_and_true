(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defn map
  [func l]
  (loop [arr []
         x l]
    (if (empty? x)
      (recur ((cons (func (first x)) arr) (rest x)))
      arr)))

(map inc [1 2 3 4])

(defmacro when
  [test & actions]
  '(if test
     (do ~@actions)))

(when true
  (print "HEY")
  (print " THERE"))

(defmacro or
  [& args]
  (loop [(x & xs) args]
    (if x
      true
      (recur xs))))

(or false false false)
(or true false false)
