(ns hbs.ext
  (:use [hbs.core :only [*hbs*]])
  (:import [com.github.jknack.handlebars Handlebars Helper Options])
  (:import [java.util Date])
  (:import [java.text SimpleDateFormat MessageFormat]))

(defmacro defhelper [name argvec & body]
  (let [argvec (into [] (concat [(gensym)] argvec))]
    `(.registerHelper ^Handlebars *hbs*
       ~(str name)
       (reify Helper
         (^CharSequence apply ~argvec ~@body)))))

(defhelper ifequals [^Object ctx ^Options options]
  (if (= ctx (.hash options "compare"))
    (.fn options ctx)
    (.inverse options ctx)))

(defhelper ifgreater [^Object ctx ^Options options]
  (if (> ctx (.hash options "compare"))
    (.fn options ctx)
    (.inverse options ctx)))

(defhelper ifless [^Object ctx ^Options options]
  (if (< ctx (.hash options "compare"))
    (.fn options ctx)
    (.inverse options ctx)))

(defhelper ifcontains [^Object ctx ^Options options]
  (if (or (contains? ctx (.hash options "item"))
        (contains? ctx (keyword (.hash options "item"))))
    (.fn options ctx)
    (.inverse options ctx)))

(defhelper uppercase [^Object ctx ^Options options]
  (clojure.string/upper-case ctx))

(defhelper lowercase [^Object ctx ^Options options]
  (clojure.string/lower-case ctx))

(defhelper or [^Object ctx ^Options options]
  (str (or ctx (.param options 0))))

(defhelper count [^Object ctx ^Options options]
  (str (count ctx)))

(defhelper format-date [^Object ctx ^Options options]
  (let [formatter (SimpleDateFormat. (.hash options "pattern"))]
    (.format ^SimpleDateFormat formatter ^Date ctx)))

(defhelper format [^Object ctx ^Options options]
  (let [pattern (.hash options "pattern")]
    (format pattern ctx)))

(defhelper ifempty [^Object ctx ^Options options]
  (if (empty? ctx)
    (.fn options ctx)
    (.inverse options ctx)))
