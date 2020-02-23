(ns hbs.core
  (:require [clojure.walk])
  (:import [com.github.jknack.handlebars
            Handlebars Template]
           [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader
            FileTemplateLoader CompositeTemplateLoader]
           [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache]))

(defn classpath-loader
  ([] (ClassPathTemplateLoader.))
  ([prefix suffix] (ClassPathTemplateLoader. prefix suffix)))

(defn file-loader [path suffix]
  (FileTemplateLoader. path suffix))

(defn composite-loader [& loaders]
  (CompositeTemplateLoader. (into-array TemplateLoader loaders)))

(defn registry [loader & {:keys [auto-reload?]}]
  (let [hbs (doto (Handlebars. loader)
              (.with (ConcurrentMapTemplateCache.)))]
    (when auto-reload?
      (.setReload (.getCache hbs) true))
    hbs))

(defonce ^:dynamic ^:no-doc *hbs*
  (registry (classpath-loader)))

(defn- wrap-context [model]
  (clojure.walk/postwalk
   #(cond
     (map? %) (java.util.HashMap. ^java.util.Map %)
     (keyword? %) (name %)
     :else %) model))

(defn render
  "Render ctx with a template provided as string."
  ([tpl ctx] (render *hbs* tpl ctx))
  ([reg tpl ctx]
   (.apply ^Template (.compileInline ^Handlebars reg tpl)
           (wrap-context ctx))))

(defn render-file
  "Render ctx with a template name."
  ([tpl-name ctx] (render-file *hbs* tpl-name ctx))
  ([reg tpl-name ctx]
   (.apply ^Template (.compile ^Handlebars reg ^String tpl-name)
           (wrap-context ctx))))

(defn wrap-hbs-template
  ([handler] (wrap-hbs-template *hbs* handler))
  ([reg handler]
   (let [process-response (fn [resp]
                            (if-let [template-info (:hbs resp)]
                              (-> resp
                                  (assoc :body (render-file reg
                                                            (:template template-info)
                                                            (:context template-info)))
                                  (update-in [:headers "Content-Type"]
                                             (fn [ct]
                                               (if (some? ct) ct "text/html; charset=utf-8")))
                                  (dissoc :hbs))
                              resp))]
     (fn
       ([req]
        (let [resp (handler req)]
          (process-response resp)))
       ([req send-response raise-error]
        (let [send-response-inner (fn [resp]
                                    (send-response (process-response resp)))]
          (handler req send-response-inner raise-error)))))))
