(ns hbs.ext
  (:refer-clojure :exclude [ hash])
  (:require [hbs.helper :refer :all])
  (:import [java.util Date])
  (:import [java.text SimpleDateFormat MessageFormat]))

(defhelper ifequals [ctx options]
  (if (= ctx (or (hash options "compare")
                 (param options 0)))
    (block-body options ctx)
    (else-body options ctx)))

(defhelper ifgreater [ctx options]
  (if (> ctx (or (hash options "compare")
                 (param options 0)))
    (block-body options ctx)
    (else-body options ctx)))

(defhelper ifless [ctx options]
  (if (< ctx (or (hash options "compare")
                 (param options 0)))
    (block-body options ctx)
    (else-body options ctx)))

(defhelper ifcontains [ ctx  options]
  (if (or (contains? ctx (hash options "item"))
          (contains? ctx (keyword (hash options "item"))))
    (block-body options ctx)
    (else-body options ctx)))

(defhelper uppercase [ctx options]
  (safe-str (clojure.string/upper-case ctx)))

(defhelper lowercase [ ctx  options]
  (safe-str (clojure.string/lower-case ctx)))

(defhelper or-helper [ ctx  options]
  (safe-str (or ctx (param options 0))))

(defhelper count-helper [ctx  options]
  (safe-str (count ctx)))

(defhelper format-date [ ctx  options]
  (let [formatter (SimpleDateFormat. (hash options "pattern"))]
    (safe-str (.format ^SimpleDateFormat formatter ^Date ctx))))

(defhelper format-helper [ ctx options]
  (let [pattern (hash options "pattern")]
    (format pattern ctx)))

(defhelper ifempty [ ctx  options]
  (if (empty? ctx)
    (block-body options ctx)
    (else-body options ctx)))

(defhelper max-helper [ ctx  options]
  (safe-str (max ctx (param options 0))))

(defhelper min-helper [ ctx  options]
  (safe-str (min ctx (param options 0))))
