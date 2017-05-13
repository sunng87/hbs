(ns hbs.server
  (:gen-class)
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [hbs.core :as hbs]))

(def reg (hbs/registry
          (hbs/classpath-loader "/templates" ".tpl")))

(defn app
  ([req] {:hbs {:template "hello"
                :context {:name "World"}}})
  ([req send-response raise-error]
   (send-response {:hbs {:template "hello"
                         :context {:name "World"}}})))

(def wrapped-app
  (->> app
       (hbs/wrap-hbs-template reg)))

(defn -main [_ & {:strs [async]}]
  (run-jetty wrapped-app {:port 7001
                          :async? async}))
