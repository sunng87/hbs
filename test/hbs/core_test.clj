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

(deftest test-render-chinese
  (testing "render some Chinese characters from file"
    (set-template-path! "/templates" ".tpl")
    (is (= "中超联赛 2015\n" (render-file "cn" {:year 2015})))))

(deftest test-render-http-file
  (testing "test render file from HTTP URL"
    (let [http-port (find-free-port)]
      (with-test-webserver http-port
        (set-template-url! (str "http://localhost:" http-port))
        (is (= "Hello World!" (render-file "hi" {:name "World"})))))))

(deftest test-middleware
  (testing "test hbs middleware"
    (set-template-path! "/templates" ".tpl")
    (let [ring-fn (fn [req] {:hbs {:template "hello"
                                  :context {:name "World"}}})
          resp ((wrap-handlebars-template ring-fn) {})]
      (is (= "Hello World!\n" (:body resp)))
      (is (= "text/html; charset=utf-8" (-> resp :headers (get "Content-Type"))))))
  (testing "test hbs async middleware"
    (set-template-path! "/templates" ".tpl")
    (let [ring-fn (fn [req send-resp _]
                    (send-resp {:hbs {:template "hello"
                                      :context {:name "World"}}}))
          test-resp-fn (fn [resp]
                         (is (= "Hello World!\n" (:body resp)))
                         (is (= "text/html; charset=utf-8" (-> resp :headers (get "Content-Type")))))]
      ((wrap-handlebars-template ring-fn) {} test-resp-fn identity))))
