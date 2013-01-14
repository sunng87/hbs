(ns hbs.ext
  (:use [hbs.core :only [*hbs*]])
  (:import [com.github.jknack.handlebars Handlebars Helper Options]))

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