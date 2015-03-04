(ns hbs.core
  (:require [clojure.walk])
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver])
  (:import [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache]))

(defonce ^:dynamic ^:no-doc *hbs*
  (doto (Handlebars. (ClassPathTemplateLoader.))
    (.with (ConcurrentMapTemplateCache.))))

(defn set-template-path!
  "Set root path where you store template file. This should be called before you render anything with hbs."
  [prefix suffix]
  (alter-var-root (var *hbs*)
                  (fn [^Handlebars h]
                    (.with h
                           (into-array TemplateLoader
                                       [(doto (ClassPathTemplateLoader.)
                                             (.setPrefix prefix)
                                             (.setSuffix suffix))])))))

(defn- wrap-context [model]
  (clojure.walk/postwalk
   #(cond
     (map? %) (java.util.HashMap. %)
     (keyword? %) (name %)
     :else %) model))

(defn render
  "Render ctx with a template provided as string."
  [tpl ctx]
  (.apply ^Template (.compileInline ^Handlebars *hbs* tpl)
          (wrap-context ctx)))

(defn render-file
  "Render ctx with a template name."
  [tpl-name ctx]
  (.apply ^Template (.compile ^Handlebars *hbs* ^String tpl-name)
          (wrap-context ctx)))

(defn wrap-handlebars-template [handler]
  (fn [req]
    (let [resp (handler req)]
      (if-let [template-info (:hbs resp)]
        (assoc resp
               :body (render-file (:template template-info)
                                  (:context template-info)))
        resp))))
