(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defn map
  [func arg]
  (loop [arr []
         elem (first arg)]
    (if elem
      (recur (cons (func elem) arr))
      arr)))

(map inc [1 2 3 4])

(defmacro when
  [test & actions]
  '(if test
     (do ~@actions)))

(when true
  (print "HEY")
  (print " THERE"))
