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
                  <title>Hello</title>
                  <author>Knuth</author>
              </book>
              <book>
                  <title>World</title>
                  <author>Knuth</author>
              </book>
              <book>
                  <title>World</title>
                  <author>Knuth</author>
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
     (let [list$ #(flatten (apply list %&))]
       (element
        (<|> (<$> #(apply merge-with list$ %) (many1 (lazy xml)))
             (stringify (many letter))))))

(defn -main []
    (println (:value (parse xml input))))

;; (-main)
