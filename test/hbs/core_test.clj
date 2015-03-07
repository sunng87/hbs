(ns hbs.core-test
  (:require [clojure.test :refer :all]
            [hbs.core :refer :all]
            [hbs.test-web-server :refer [find-free-port with-test-webserver]])
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

(deftest test-render-file
  (testing "test render from file"
    (set-template-path! "/templates" ".tpl")
    (is (= "Hello World!\n" (render-file "hello" {:name "World"})))))

(deftest test-render-http-file
  (testing "test render file from HTTP URL"
    (let [http-port (find-free-port)]
      (with-test-webserver http-port
        (set-template-url! (str "http://localhost:" http-port))
        (is (= "Hello World!" (render-file "hi" {:name "World"})))))))
