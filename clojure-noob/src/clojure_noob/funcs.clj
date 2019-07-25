(ns clojure-noob.funcs)

; map can take multiple collections and iterate over them at the same time
(map + [1 2] [3 4])

; into is just a lifted conj
(defn my-into [dest coll] (apply conj dest coll))
(my-into {} [[:a "hi"]])
