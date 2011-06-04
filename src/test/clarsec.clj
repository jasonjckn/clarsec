(ns test.clarsec
  (:use
   [eu.dnetlib.clojure.clarsec]
   [eu.dnetlib.clojure.monad]
   [clojure.test]))

(deftest test-recur1
  (def recur1 
       (<|> (symb "foo")
            (braces (lazy recur1))))

  (let [parse$ #(or (:value (parse recur1 %)) :fail)]
    (is (= (parse$ "foo") "foo"))
    (is (= (parse$ "{foo}") "foo"))
    (is (= (parse$ "{{foo}}") "foo"))
    (is (= (parse$ "{{{foo}}}") "foo"))
    (is (= (parse$ "bar") :fail))
    (is (= (parse$ "{bar}") :fail))))

(deftest test-recur2
  (defn recur2 [x]
    (<|> (symb x)
         (braces (lazy (recur2 x)))))

  (let [parse$ #(or (:value (parse (recur2 "bar") %)) :fail)]
    (is (= (parse$ "bar") "bar"))
    (is (= (parse$ "{bar}") "bar"))
    (is (= (parse$ "{{bar}}") "bar"))
    (is (= (parse$ "{{{bar}}}") "bar"))
    (is (= (parse$ "foo") :fail))
    (is (= (parse$ "{foo}") :fail))))

(deftest test-recur3
  (defn recur3 [x]
    (<|> (symb (if (= (mod x 2) 0) "foo" "bar"))
         (braces (lazy (recur3 (inc x))))))

  (let [parse$ #(or (:value (parse (recur3 0) %)) :fail)]
    (is (= (parse$ "foo") "foo"))
    (is (= (parse$ "bar") :fail))

    (is (= (parse$ "{foo}") :fail))
    (is (= (parse$ "{bar}") "bar"))

    (is (= (parse$ "{{foo}}") "foo"))
    (is (= (parse$ "{{bar}}") :fail))

    (is (= (parse$ "{{{foo}}}") :fail))
    (is (= (parse$ "{{{bar}}}") "bar"))))


(deftest test-unbound-var
  (def unbound-var)
  (def fwdref1 (lazy unbound-var))
  (def unbound-var identifier)

  (let [parse$ #(or (:value (parse fwdref1 %)) :fail)]
    (is (= (parse$ "foo") "foo"))
    (is (= (parse$ "9foo") :fail))))
