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

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(symmetrize-body-parts asym-hobbit-body-parts)

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce #(into %1 (set [%2 (matching-part %2)])) [] asym-body-parts))

; (better-symmetrize-body-parts asym-hobbit-body-parts)

(defn hit
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(hit asym-hobbit-body-parts)
