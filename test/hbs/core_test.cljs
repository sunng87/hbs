(ns hbs.core-test
  (:require [hbs.core :as hbs]
            [cljs.test :refer-macros [deftest is testing] :as t]))

(deftest test-hbs
  (testing "base case"
    (let [t (hbs/compile "hello {{name}}")]
      (is (= "hello world" (hbs/render t {:name "world"}))))))
