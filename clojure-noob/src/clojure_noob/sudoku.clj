(ns clojure-noob.sudoku
  (:require [clojure.set :as set])
  (:require [clojure.string :as str])
  (:gen-class))

(defn has? [coll val]
  (some #{val} coll))

(defn value [[name {value :value}]] value)

(defn taken [row]
  (set (remove nil? (map value row))))

(defn available [row]
  (set/difference #{1 2 3 4 5 6 7 8 9} (taken row)))

(defn update-square [possibilities {value :value :as square}]
  (if value
    square
    (if (= 1 (count possibilities))
      (assoc square :value (first possibilities))
      (assoc square :possibilities possibilities))))

(defn update-row
  ([row] (map #(update-row (available row) %) row))
  ([possibilities [name square]]
   [name (update-square possibilities square)]))

(defn update-rows [rows]
  (map update-row rows))

(defn merge-squares [a b]
  (or
   (if (has? a :value) a)
   (if (has? b :value) b)
   (if (and
        (has? a :possibilities)
        (has? b :possibilities))
     {:possibilities (set/intersection (set (get a :possibilities)) (set (get b :possibilities)))}
     (or
      (if (has? a :possibilities) a)
      (if (has? b :possibilities) b)))))

(defn merge-rows [rows]
  (merge-with merge-squares rows))

(defn to-sudoku
  ([parts] (into {} (mapcat
                     (fn [[k v]] (to-sudoku k (str v)))
                     (zipmap ["a" "b" "c" "d" "e" "f" "g" "h" "i"] parts))))
  ([row part] (map-indexed
               (fn [idx itm]
                 [(str row (inc idx))
                  (if-not (= itm \*) {:value (read-string (str itm))} {})])
               (seq part))))

(defn load-sudoku [filepath]
  (to-sudoku (str/split (str/trim (slurp filepath)) #"\n")))

(def solved (load-sudoku "samples/01-solved.txt"))

(defn horizontal-rows [sudoku]
  (vals (group-by (fn [[name square]] (take 1 name)) sudoku)))

(defn vertical-rows [sudoku]
  (vals (group-by (fn [[name square]] (take 1 (str/reverse name))) sudoku)))

(def square
  [["a1" "a2" "a3" "b1" "b2" "b3" "c1" "c2" "c3"]
   ["d1" "d2" "d3" "e1" "e2" "e3" "f1" "f2" "f3"]
   ["g1" "g2" "g3" "h1" "h2" "h3" "i1" "i2" "i3"]
   ["a4" "a5" "a6" "b4" "b5" "b6" "c4" "c5" "c6"]
   ["d4" "d5" "d6" "e4" "e5" "e6" "f4" "f5" "f6"]
   ["g4" "g5" "g6" "h4" "h5" "h6" "i4" "i5" "i6"]
   ["a7" "a8" "a9" "b7" "b8" "b9" "c7" "c8" "c9"]
   ["d7" "d8" "d9" "e7" "e8" "e9" "f7" "f8" "f9"]
   ["g7" "g8" "g9" "h7" "h8" "h9" "i7" "i8" "i9"]])

(defn square-group [name]
  (first (filter #(some #{name} %) square)))

(defn square-rows [sudoku]
  (vals
   (group-by
    (fn [[name _]] (square-group name))
    sudoku)))

(defn rows [sudoku]
  (concat (horizontal-rows sudoku) (vertical-rows sudoku) (square-rows sudoku)))

(defn row-complete? [row]
  (= (count (taken row)) 9))

(defn solved? [sudoku]
  (every? row-complete? (rows sudoku)))

(def one-open (load-sudoku "samples/02-one-open.txt"))

(->> one-open
     update-rows
     merge-rows
     solved?)
