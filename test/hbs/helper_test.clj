(ns hbs.helper-test
  (:require [clojure.test :refer :all]
            [hbs.core :refer :all]
            [hbs.helper :refer :all :exclude [hash]]
            [clojure.edn])
  (:import [com.github.jknack.handlebars Handlebars]
           [com.github.jknack.handlebars.io ClassPathTemplateLoader]
           [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache]))

(deftest test-use-javascript-helpers
  (testing "test usage of javascript helpers"
    (is (= "bar"
           (do
             (register-js-helpers! "dev-resources/helpers/helpers.js")
             (render "{{js-helper foo}}" {:foo "bar"}))))))

(deftest defhelper-test
  (testing "defines a custom helper"
    (binding [*hbs* (doto (Handlebars. (ClassPathTemplateLoader.))
                      (.with (ConcurrentMapTemplateCache.)))]
      (let [context {:foo "bar"}
            params ["baz" "qux"]]
        (defhelper test-helper
          [ctx options]
          (safe-str
           (pr-str {:name (.-helperName options)
                    :ctx ctx
                    :params (into [] (.-params options))})))
        (register-helper! *hbs* "test-helper" test-helper)
        (is (= {:name "test-helper"
                :ctx (:foo context)
                :params params}
               (clojure.edn/read-string (render "{{test-helper foo baz qux}}"
                                                {:foo "bar" :baz "baz" :qux "qux"}))))))))

(deftest defhelper-missing-test
  (testing "defines the helper that gets used when none of that name exists"
    (binding [*hbs* (doto (Handlebars. (ClassPathTemplateLoader.))
                      (.with (ConcurrentMapTemplateCache.)))]
      (let [context {:foo "bar"}
            params ["baz" "qux"]]
        (register-helper-missing! *hbs*
                                  (helper [ctx options] (safe-str
                                                         (pr-str
                                                          {:name (.-helperName options)
                                                           :ctx ctx
                                                           :params (into [] (.-params options))}))))
        (is (= {:name "missing-helper"
                :ctx (:foo context)
                :params params}
               (clojure.edn/read-string (render "{{missing-helper foo baz qux}}"
                                                {:foo "bar" :baz "baz" :qux "qux"}))))))))
