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

(defn what-i-need
  [{:keys [api-key]}]
  (str "found it " api-key "!"))

(what-i-need {:api-key "SUPERSECRET" :other-stuff 123})

; note that there is no way to require that the specified keys are in the passed map
(defn what-i-wish-i-had
  [{magic-words :magic-words}]
  (if-not (nil? magic-words)
    (str "found it " magic-words "!")))

(what-i-wish-i-had {:boring-stuff 42})

(def transforms [#(+ 5 %) #(* % 32) inc dec #(+ % 4) #(- % 3)])

(reduce #(%2 %1) 4 transforms)
