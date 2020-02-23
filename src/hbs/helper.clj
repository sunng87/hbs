(ns hbs.helper
  (:refer-clojure :exclude [hash])
  (:require [clojure.java.io :as io])
  (:require [hbs.core :refer [*hbs*]])
  (:import [com.github.jknack.handlebars
            Handlebars Helper Options Handlebars$SafeString]))

(defmacro helper
  "create a helper instance"
  [argvec & body]
  (let [argvec (into [] (concat [(gensym)] argvec))]
    `(reify Helper (apply ~argvec ~@body))))

(defmacro defhelper
  "def named helper."
  [name argvec & body]
  (let [argvec (into [] (concat [(gensym)] argvec))]
    `(def ~name (reify Helper (apply ~argvec ~@body)))))

(defn register-helper! [^Handlebars reg ^String name ^Helper helper]
  (.registerHelper reg name helper))

(defn register-helper-missing! [^Handlebars reg ^Helper helper]
  (.registerHelperMissing reg helper))

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
  ([path] (register-js-helpers! *hbs* path))
  ([reg path] (.registerHelpers ^Handlebars reg (io/file path))))
