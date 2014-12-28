(ns hbs.helper
  (:refer-clojure :exclude [hash])
  (:use [hbs.core :only [*hbs*]])
  (:import [com.github.jknack.handlebars
            Handlebars Helper Options Handlebars$SafeString]))

(defmacro defhelper
  "Macro for helper definition."
  [name argvec & body]
  (let [argvec (into [] (concat [(gensym)] argvec))]
    `(.registerHelper ^Handlebars *hbs*
       ~(str name)
       (reify Helper
         (apply ~argvec ~@body)))))

(defprotocol HandlebarsOptions
  (param [this idx])
  (hash [this key])
  (block-body [this ctx])
  (else-body [this ctx]))

(extend-protocol HandlebarsOptions
  Options
  (param [this idx]
    (.param this idx))
  (hash [this key]
    (.hash this key))
  (block-body [this ctx]
    (.fn this ctx))
  (else-body [this ctx]
    (.inverse this ctx)))

(defn safe-str
  "A wrapper of Handlebars.SafeString"
  [& text]
  (Handlebars$SafeString. (apply str text)))

(defn register-js-helpers!
  "Register helper defined in JavaScript."
  [path]
  (.registerHelpers ^Handlebars *hbs* (clojure.java.io/file path)))
