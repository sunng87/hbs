(ns hbs.helper-test
  (:use clojure.test
        hbs.core
        hbs.helper))

(deftest test-use-javascript-helpers
  (testing "test usage of javascript helpers"
    (is (= "bar"
          (do
            (register-js-helpers! "dev-resources/helpers/helpers.js")
            (render "{{js-helper foo}}" {:foo "bar"}))))))