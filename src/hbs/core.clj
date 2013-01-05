(ns hbs.core
  (:import [java.net URI])
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver])
  (:import [com.github.jknack.handlebars.io ClassTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapCache])
  (:import [hbs KeywordMapValueResolver]))

(def ^:dynamic *hbs*
  (Handlebars. (ClassTemplateLoader.)
               (ConcurrentMapCache.)))

(defn set-template-path! [prefix suffix]
  (alter-var-root *hbs*
                  (fn [] (Handlebars. (doto (ClassTemplateLoader.)
                                       (.setPrefix prefix)
                                       (.setSuffix suffix))
                                     (ConcurrentMapCache.)))))

(defn wrap-context [model]
  (-> (Context/newBuilder model)
    (.resolver (into-array ValueResolver [KeywordMapValueResolver/INSTANCE]))
    (.build)))

(defn render [tpl ctx]
  (.apply ^Template (.compile ^Handlebars *hbs* tpl)
          (wrap-context ctx)))

(defn render-file [tpl-name ctx]
  (.apply ^Template (.compile ^Handlebars *hbs*
                              (URI/create tpl-name))
          (wrap-context ctx)))

