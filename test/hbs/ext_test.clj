(ns hbs.ext-test
  (:use [clojure.test]
        [hbs core ext]))

(deftest test-ifequals
  (let [tpl "{{#ifequals name compare=\"tomcat\"}}hellworld{{else}}nice{{/ifequals}}"]
    (is (= "hellworld" (render tpl {:name "tomcat"})))
    (is (= "nice" (render tpl {:name "not-a-tomcat"})))))

(deftest test-ifgreater
  (let [tpl "{{#ifgreater name compare=0}}hellworld{{else}}nice{{/ifgreater}}"]
    (is (= "hellworld" (render tpl {:name 23})))
    (is (= "nice" (render tpl {:name -23})))))

(deftest test-ifless
  (let [tpl "{{#ifless name compare=0}}hellworld{{else}}nice{{/ifless}}"]
    (is (= "hellworld" (render tpl {:name -23})))
    (is (= "nice" (render tpl {:name 23})))))

(deftest test-ifcontains
  (let [tpl "{{#ifcontains set item=\"hello\"}}hello{{/ifcontains}}"]
    (is (= "hello" (render tpl {:set #{:hello}})))
    (is (= "hello" (render tpl {:set #{"hello"}})))))

(deftest test-uppercase
  (let [tpl "{{uppercase tom.cat}}"]
    (is (= "HELLO" (render tpl {:tom {:cat "hello"}})))))

(deftest test-lowercase
  (let [tpl "{{lowercase tom.cat}}"]
    (is (= "hello" (render tpl {:tom {:cat "HELLO"}})))))


(deftest test-or
  (let [tpl "{{or tom.cat 100}}"]
    (is (= "100" (render tpl {:tom {}})))))

(deftest test-count
  (let [tpl "{{count tom.cat}}"]
    (is (= "10" (render tpl {:tom {:cat (range 10)}})))))

(deftest test-format
  (let [tpl "{{format tom.cat pattern=\"%.2f\"}}"]
    (is (= "3.33" (render tpl {:tom {:cat 3.333}})))))


(deftest test-ifempty
  (let [tpl "{{#ifempty this}}helloworld{{else}}nice{{/ifempty}}"]
    (is (= "helloworld" (render tpl {})))
    (is (= "nice" (render tpl {:a 1})))))
