(ns hbs.core
  (:import [java.net URI])
  (:import [com.github.jknack.handlebars Handlebars Template])
  (:import [com.github.jknack.handlebars.io ClassTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapCache]))

(def ^:dynamic *hbs*
  (Handlebars. (ClassTemplateLoader.)
               (ConcurrentMapCache.)))

(defn set-template-path! [prefix suffix]
  (alter-var-root *hbs*
                  (fn [] (Handlebars. (doto (ClassTemplateLoader.)
                                       (.setPrefix prefix)
                                       (.setSuffix suffix))
                                     (ConcurrentMapCache.)))))

(defn wrap-context [ctx]
  (if (map? ctx)
    (into {} (for [[k v] ctx]
               [(if (keyword? k) (name k) (str k)) v]))
    ctx))

(defn render [tpl ctx]
  (.apply ^Template (.compile ^Handlebars *hbs* tpl)
          (wrap-context ctx)))

(defn render-file [tpl-name ctx]
  (.apply ^Template (.compile ^Handlebars *hbs*
                              (URI/create tpl-name))
          (wrap-context ctx)))

