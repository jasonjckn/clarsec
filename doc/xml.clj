(ns xml
  (:use
   [eu.dnetlib.clojure.clarsec]
   [eu.dnetlib.clojure.monad]))

;; Parsing simplified XML:
;;
;; Sample Input:

(def input
     "    <library>
              <book>
                  <title>Joy of Clojure</title>
                  <author>Fogus</author>
              </book>
              <book>
                  <title>Structured and Interpretation of Computer Programs</title>
                  <author>MIT</author>
              </book>
          </library>")

(defn arrows [p] (between (symb "<") (symb ">") p))

(def open-tag (arrows identifier))
(defn close-tag [expect-name] (arrows (symb (str "/" expect-name))))

(defn element [p]
  (let-bind [tag-name open-tag
             contents p
             _ (close-tag tag-name)]
            (result {(keyword tag-name) contents})))

(def xml
     (let [list$ #(flatten (list %&))]
       (element
        (<|> (<$> #(apply merge-with list$ %) (many1 (lazy xml)))
             (stringify (many (<|> letter space)))))))

(defn -main []
    (prn (:value (parse xml input))))

;; (-main)

;; Output:
;; {:library 
;;    {:book ({:author "Fogus",
;;             :title "Joy of Clojure"} 
;;            {:author "MIT",
;;             :title "Structured and Interpretation of Computer Programs"})}}
