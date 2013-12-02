(ns hbs.core
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver])
  (:import [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache])
  (:import [hbs KeywordMapValueResolver]))

(defonce ^:dynamic *hbs*
  (doto (Handlebars. (ClassPathTemplateLoader.))
    (.with (ConcurrentMapTemplateCache.))))

(defn set-template-path! [prefix suffix]
  (alter-var-root (var *hbs*)
                  (fn [h]
                    (.with h
                           (into-array TemplateLoader
                                       [(doto (ClassPathTemplateLoader.)
                                             (.setPrefix prefix)
                                             (.setSuffix suffix))])))))

(defn wrap-context [model]
  (-> (Context/newBuilder model)
    (.resolver (into-array ValueResolver [KeywordMapValueResolver/INSTANCE]))
    (.build)))

(defn render [tpl ctx]
  (.apply ^Template (.compileInline ^Handlebars *hbs* tpl)
          (wrap-context ctx)))

(defn render-file [tpl-name ctx]
  (.apply ^Template (.compile ^Handlebars *hbs* tpl-name)
          (wrap-context ctx)))
