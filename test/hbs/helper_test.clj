(ns hbs.helper-test
  (:require [clojure.test :refer :all] 
            [hbs.core :refer :all] 
            [hbs.helper :refer :all :exclude [hash]]))

(deftest test-use-javascript-helpers
  (testing "test usage of javascript helpers"
    (is (= "bar"
           (do
             (register-js-helpers! "dev-resources/helpers/helpers.js")
             (render "{{js-helper foo}}" {:foo "bar"}))))))
