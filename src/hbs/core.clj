(ns hbs.core
  (:require [clojure.walk])
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver]
           [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader URLTemplateLoader]
           [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache]))

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

(defn set-reload!
  "Allow template auto reload, disabled by default"
  [reload?] (.setReload (.getCache *hbs*) reload?))

(defn url-template-loader []
  (proxy [URLTemplateLoader] []
    (getResource [location] (java.net.URL. location))))

(defn set-template-url!
  "Set root URL where you store template files. This should be called before you render anything with hbs."
  ([prefix]
   (set-template-url! prefix URLTemplateLoader/DEFAULT_SUFFIX))
  ([prefix suffix]
   (let [template-loader (url-template-loader)]
     (alter-var-root (var *hbs*)
                     (fn [^Handlebars h]
                       (.with h
                              (into-array TemplateLoader
                                          [(doto template-loader
                                             (.setPrefix prefix)
                                             (.setSuffix suffix))])))))))

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
        (-> resp
            (assoc :body (render-file (:template template-info)
                                      (:context template-info)))
            (update-in [:headers "Content-Type"]
                       (fn [ct]
                         (if (some? ct) ct "text/html; charset=utf-8"))))

        resp))))
