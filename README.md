# hbs

Real-world Clojure templating, seriously. Don't talk about enlive or
hiccup on a clojure web development book any more.

* Never ruin your Clojure code with HTML
* Never ruin your HTML code with Clojure
* Templating without reuse mechanism is shit
* Templating without customization is nothing but shit

What handlebars has?

* Separate your Clojure and HTML, calm down both
* Reuse your handlebars template with **include**, **partial** and **block**
* Create your own **helpers** for your infinite customization needs

This library is based on [handlebars.java](https://github.com/jknack/handlebars.java/).

[![Build Status](https://travis-ci.org/sunng87/hbs.svg?branch=master)](https://travis-ci.org/sunng87/hbs)

## Usage

### Leiningen

![https://clojars.org/link](https://clojars.org/hbs/latest-version.svg)

### Using hbs.core

```clojure
(use '[hbs.core])

;; render something from template string
(render "foo {{foo}}" {:foo "bar"})

;; configure where to load template file:
(set-template-path! "templates" ".html")

;; OR load templates from a remote URL (defaults to .hbs files):
(set-template-url! "http://example.com/templates/")

;; render something from templates/index.html
(render-file "index" {:foo "bar"})
```

### Using hbs in Ring application

hbs library provides a default ring middleware
`hbs.core/wrap-handlebars-template` to generate pages from standard
response: `{:hbs {:template ... :context ...}}`

```clojure
;; ring handler function that generate handlebars response
(defn some-handler [req]
  ;;...
  {:hbs {:template "index"
         :context {:a "A" :b "B"}}})
```

By default, hbs middleware use `text/html; charset=utf-8` as content
type. You can override it by setting `{:headers {"Content-Type" ...}}`
explicitly in your response.

### Extending hbs.helper

Handlebars is nothing without **helpers**.

```clojure
(use '[hbs core helper])

(defhelper mytag [ctx options]
  (safe-str "HelloWorld, " (clojure.string/upper ctx)))

(render "{{mytag foo}}" {:foo "bar"})

```

### Using javascript helpers
Helpers can also be defined using javascript. Javascript helpers
are registered using register-js-helpers!-function
```clojure
(register-js-helpers! "path/to/file.js")
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

You can find usage examples of these helpers in the test case `test/hbs/ext_test.clj`.

### API Documents

[Documents](http://sunng.info/hbs/)

## See also

Handlebars implemented for Rust language: [handlebars-rust](https://github.com/sunng87/handlebars-rust).

## License

Copyright © 2013-2015 Sun Ning

Distributed under the Eclipse Public License, the same as Clojure.
