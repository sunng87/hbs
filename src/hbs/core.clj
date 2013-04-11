(ns hbs.core
  (:import [java.net URI])
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver])
  (:import [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache])
  (:import [hbs KeywordMapValueResolver]))

(defonce ^:dynamic *hbs*
  (Handlebars. (ClassPathTemplateLoader.)
               (ConcurrentMapTemplateCache.)))

(defn set-template-path! [prefix suffix]
  (alter-var-root (var *hbs*)
                  (fn [h]
                    (.with h
                           ^TemplateLoader (doto (ClassPathTemplateLoader.)
                                             (.setPrefix prefix)
                                             (.setSuffix suffix))))))

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

