(ns test.clarsec
  (:use
   [eu.dnetlib.clojure.clarsec]
   [eu.dnetlib.clojure.monad]
   [clojure.test]))

(def recur1 
     (<|> (symb "foo")
          (braces (m-lazy recur1))))

(defn recur2 [x]
  (<|> (symb x)
       (braces (m-lazy (recur2 x)))))

(defn recur3 [x]
  (<|> (symb (if (= (mod x 2) 0) "foo" "bar"))
       (braces (m-lazy (recur3 (inc x))))))


(deftest test-recur1
  (let [parse$ #(or (:value (parse recur1 %)) :fail)]
    (is (= (parse$ "foo") "foo"))
    (is (= (parse$ "{foo}") "foo"))
    (is (= (parse$ "{{foo}}") "foo"))
    (is (= (parse$ "{{{foo}}}") "foo"))
    (is (= (parse$ "bar") :fail))
    (is (= (parse$ "{bar}") :fail))))

(deftest test-recur2
  (let [parse$ #(or (:value (parse (recur2 "bar") %)) :fail)]
    (is (= (parse$ "bar") "bar"))
    (is (= (parse$ "{bar}") "bar"))
    (is (= (parse$ "{{bar}}") "bar"))
    (is (= (parse$ "{{{bar}}}") "bar"))
    (is (= (parse$ "foo") :fail))
    (is (= (parse$ "{foo}") :fail))))

(deftest test-recur3
  (let [parse$ #(or (:value (parse (recur3 0) %)) :fail)]
    (is (= (parse$ "foo") "foo"))
    (is (= (parse$ "bar") :fail))

    (is (= (parse$ "{foo}") :fail))
    (is (= (parse$ "{bar}") "bar"))

    (is (= (parse$ "{{foo}}") "foo"))
    (is (= (parse$ "{{bar}}") :fail))

    (is (= (parse$ "{{{foo}}}") :fail))
    (is (= (parse$ "{{{bar}}}") "bar"))))


