# hbs

Real-world Clojure templating, seriously. Don't talk about enlive or
hiccup on a clojure web development book any more.

Based on [handlebars.java](https://github.com/jknack/handlebars.java/)

## Usage

### Leiningen

```
[hbs "0.4.1"]
```

### Using hbs.core

```clojure
(use '[hbs.core])

;; render something from template string
(render "foo {{foo}}" {:foo "bar"})

;; configure where to load template file:
(set-template-path! "templates" ".html") 

;; render something from templates/index.html
(render-file "index" {:foo "bar"})
```

### Extending hbs.helper

Handlebars is nothing without **helpers**.

```clojure
(use '[hbs core helper])

(defhelper mytag [ctx options]
  (safe-str "HelloWorld, " (clojure.string/upper ctx)))

(render "{{mytag foo}}" {:foo "bar"})

```

### Helpers defined by me

I have some predefined helpers used in my projects. And I decide to
ship it in the release.

To use these helpers, be sure eval `hbs.ext`. You can
just add a `(:require [hbs.ext])` on you core namespace.

Available helpers:

* ifequals
* ifgreater
* ifless
* ifcontains
* uppercase
* lowercase
* or
* count
* format-date
* format
* ifempty
* max
* min

## License

Copyright © 2013 Sun Ning

Distributed under the Eclipse Public License, the same as Clojure.
