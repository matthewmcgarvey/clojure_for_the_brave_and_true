(ns clojure-noob.sudoku
  (:require
   [clojure.string :as str]
   [clojure.set :as set]))

(defn read-in [filepath]
  (str/split (str/trim (slurp filepath)) #"\n"))

(defn split-row-string [row-string]
  (str/split row-string #""))

(defn parse-row-val [row-val]
  (if (re-matches #"^\d+$" row-val)
    (Integer. row-val)
    nil))

(defn parse-row-string [row-string]
  (into [] (map parse-row-val (split-row-string row-string))))

(defn parse-sudoku-file [filepath]
  (into [] (map parse-row-string (read-in filepath))))

(defn taken [row]
  (into #{} (remove nil? row)))

(defn possibilities
  ([row] (set/difference #{1 2 3 4 5 6 7 8 9} (taken row)))
  ([row & rows] (apply set/intersection (possibilities row) (map possibilities rows))))

(defn val-at-position [sudoku [x y]]
  (nth (nth sudoku y) x))

(defn horizontal-row-at-position [sudoku [_ y]]
  (nth sudoku y))

(def puzzle (parse-sudoku-file "samples/01-solved.txt"))
(def unsolved-puzzle (parse-sudoku-file "samples/02-one-open.txt"))

(defn vertical-row-at-position [sudoku [x _]]
  (map #(nth % x) sudoku))

(def square
  [[[0 0] [0 1] [0 2] [1 0] [1 1] [1 2] [2 0] [2 1] [2 2]]
   [[3 0] [3 1] [3 2] [4 0] [4 1] [4 2] [5 0] [5 1] [5 2]]
   [[6 0] [6 1] [6 2] [7 0] [7 1] [7 2] [8 0] [8 1] [8 2]]
   [[0 3] [0 4] [0 5] [1 3] [1 4] [1 5] [2 3] [2 4] [2 5]]
   [[3 3] [3 4] [3 5] [4 3] [4 4] [4 5] [5 3] [5 4] [5 5]]
   [[6 3] [6 4] [6 5] [7 3] [7 4] [7 5] [8 3] [8 4] [8 5]]
   [[0 6] [0 7] [0 8] [1 6] [1 7] [1 8] [2 6] [2 7] [2 8]]
   [[3 6] [3 7] [3 8] [4 6] [4 7] [4 8] [5 6] [5 7] [5 8]]
   [[6 6] [6 7] [6 8] [7 6] [7 7] [7 8] [8 6] [8 7] [8 8]]])

(defn square-positions [position]
  (first (filter #(some #{position} %) square)))

(defn square-row-at-position [sudoku position]
  (let [row-positions (square-positions position)]
    (map #(val-at-position sudoku %) row-positions)))

(defn row-solved? [row]
  (= (count (taken row)) 9))

(defn sudoku-solved? [sudoku]
  (every? row-solved? sudoku))

(defn rows-connected-to-position [sudoku position]
  (map #(% sudoku position) [horizontal-row-at-position vertical-row-at-position square-row-at-position]))

(defn position-solved? [sudoku position]
  (some? (val-at-position sudoku position)))

(defn unsolved-positions [sudoku]
  (remove #(position-solved? sudoku %) (mapcat identity square)))

(defn possibilities-for-position [sudoku position]
  (apply possibilities (rows-connected-to-position sudoku position)))

(defn set-value-at-position [sudoku [x y] value]
  (assoc-in sudoku [y x] value))

(defn update-sudoku-for-position [sudoku position]
  (let [possies (possibilities-for-position sudoku position)]
    (if (= (count possies) 1)
      (set-value-at-position sudoku position (first possies))
      sudoku)))

(defn number-of-possibilities [sudoku position]
  (count (possibilities-for-position sudoku position)))

(defn position-has-one-possibility? [sudoku position]
  (= (number-of-possibilities sudoku position) 1))

(defn find-positions-with-one-possibility [sudoku]
  (filter #(position-has-one-possibility? sudoku %) (unsolved-positions sudoku)))

(defn find-first-unsolved-position-with-one-possibility [sudoku]
  (first (find-positions-with-one-possibility sudoku)))

(defn update-sudoku
  ([sudoku]
   (update-sudoku sudoku (find-first-unsolved-position-with-one-possibility sudoku)))
  ([sudoku position]
   (if (some? position)
     (update-sudoku-for-position sudoku position)
     sudoku)))

(defn val-to-s [val]
  (if (some? val)
    (str val)
    "*"))

(defn row-to-s [row]
  (str/join "" (map val-to-s row)))

(defn sudoku-to-s [sudoku]
  (str/join "\n" (map row-to-s sudoku)))

(defn write-to-file
  ([filename sudoku]
   (spit filename (sudoku-to-s sudoku)))
  ([filename sudoku & sudokus]
   (spit filename (str/join "\n\n" (map sudoku-to-s (cons sudoku sudokus))))))

(defn attempt [sudoku]
  (loop [[curr & prev :as chain] [sudoku]]
    (if (= curr (first prev))
      prev
      (recur (cons (update-sudoku curr) chain)))))

(defn print-results [states]
  (do
    (if (sudoku-solved? (first states))
      (println "Solved")
      (println "Failed to solve"))
    (apply write-to-file "result.txt" states)))

(defn do-the-thing [filepath]
  (print-results (attempt (parse-sudoku-file filepath))))

(do-the-thing "samples/04.txt")

