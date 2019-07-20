(ns clojure-noob.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "I'm a little teapot!"))

(defn map
  [func l]
  (loop [arr []
         [x & xs] l]
    (if (nil? x)
      (recur (cons (func x) arr) xs)
      arr)))

(map inc [1 2 3 4])

(defmacro when
  [test & actions]
  `(if test
     (do ~@actions)))

(when true
  (print "HEY")
  (print " THERE"))

(defmacro or
  [& args]
  `(loop [[x & xs] ~@args]
     (if x
       true
       (if (empty? xs)
         x
         (recur xs)))))

; (or false false false)
; (or true false false)
; (or false false nil)
; (or "hi" false false)

(def x {:logic inc})

((get x :logic) 4)

((x :unlogic dec) 6)

(:logic x)

(:logic nil)
