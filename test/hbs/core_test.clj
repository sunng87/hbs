(ns hbs.core-test
  (:require [clojure.test :refer :all]
            [hbs.core :refer :all])
  (:import [java.util HashMap]))

(def reg (registry (classpath-loader "/templates" ".tpl")))

(deftest test-render
  (testing "test inline render"
    (is (= "Hello World" (render "Hello {{person.name}}"
                                 {:person {:name "World"}})))))

(deftest test-render-juh
  (let [p (doto (HashMap.)
            (.put "name" "Handlebars"))]
    (is (= "Handlebars" (render "{{#person}}{{name}}{{/person}}"
                                {:person p})))))

(deftest test-render-file
  (testing "test render from file"
    (is (= "Hello World!\n" (render-file reg "hello" {:name "World"})))))

(deftest test-render-chinese
  (testing "render some Chinese characters from file"
    (is (= "中超联赛 2015\n" (render-file reg "cn" {:year 2015})))))

(deftest test-middleware
  (testing "test hbs middleware"
    (let [ring-fn (fn [req] {:hbs {:template "hello"
                                  :context {:name "World"}}})
          resp ((wrap-handlebars-template reg ring-fn) {})]
      (is (= "Hello World!\n" (:body resp)))
      (is (= "text/html; charset=utf-8" (-> resp :headers (get "Content-Type"))))))
  (testing "test hbs async middleware"
    (let [ring-fn (fn [req send-resp _]
                    (send-resp {:hbs {:template "hello"
                                      :context {:name "World"}}}))
          test-resp-fn (fn [resp]
                         (is (= "Hello World!\n" (:body resp)))
                         (is (= "text/html; charset=utf-8" (-> resp :headers (get "Content-Type")))))]
      ((wrap-handlebars-template reg ring-fn) {} test-resp-fn identity))))
