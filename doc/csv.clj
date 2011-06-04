(ns csv
  (:use
   [eu.dnetlib.clojure.clarsec]
   [eu.dnetlib.clojure.monad]))

;; Parsing simplified XML:
;;
;; Sample Input:

(def input
     "Year,Make,Model,Length
1997,Ford,Model-350,234
2000,Mercury,\"Model 800\",238")

(def cell (<|> stringLiteral
               (stringify (many (none-of ",\n")))))
(def line (sep-by cell comma))
(def csv (sep-by line eol))

(defn -main []
  (prn (:value (parse csv input))))

;; Output:
;; 
;; (("Year" "Make" "Model" "Length")
;;  ("1997" "Ford" "Model-350" "234")
;;  ("2000" "Mercury" "Model 800" "238"))

