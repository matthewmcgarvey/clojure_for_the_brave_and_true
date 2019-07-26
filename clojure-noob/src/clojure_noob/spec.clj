(ns clojure-noob.spec
  (:require
   [clojure.spec.alpha :as s]
   [clojure.repl :as repl]))

(s/def ::suit #{:club :spade :diamond :heart})

(s/conform ::suit "LOL")
(repl/doc ::suit)
