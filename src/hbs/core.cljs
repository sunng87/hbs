(ns hbs.core
  (:require [cljs.nodejs :as node]))

(def handlebars-js (node/require "handlebars"))

(defn compile [template-string]
  (.compile handlebars-js template-string))

(defn render [template-fn context]
  (template-fn (clj->js context)))
