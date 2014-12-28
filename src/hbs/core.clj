(ns hbs.core
  (:import [com.github.jknack.handlebars
            Handlebars Template Context ValueResolver])
  (:import [com.github.jknack.handlebars.io TemplateLoader ClassPathTemplateLoader])
  (:import [com.github.jknack.handlebars.cache ConcurrentMapTemplateCache])
  (:import [hbs KeywordMapValueResolver]))

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
  (-> (Context/newBuilder model)
    (.resolver (into-array ValueResolver [KeywordMapValueResolver/INSTANCE]))
    (.build)))

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
