(ns hbs.core-test
  (:require [clojure.test :refer :all]
            [hbs.core :refer :all])
  (:import [java.util HashMap]))

(deftest test-render
  (testing "test inline render"
    (is (= "Hello World" (render "Hello {{person.name}}"
                                 {:person {:name "World"}})))))

(deftest test-render-juh
  (let [p (doto (HashMap.)
            (.put "name" "Handlebars"))]
    (is (= "Handlebars" (render "{{#person}}{{name}}{{/person}}"
                                {:person p})))))

(set-template-path! "/templates" ".tpl")

(deftest test-render-file
  (testing "test render from file"
    (is (= "Hello World!\n" (render-file "hello" {:name "World"})))))

(deftest test-render-chinese
  (testing "render some Chinese characters from file"
    (is (= "中超联赛 2015\n" (render-file "cn" {:year 2015})))))
