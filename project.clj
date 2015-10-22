(defproject hbs "0.8.3"
  :description "Clojure templating by Handlebars.java"
  :url "http://github.com/sunng87/hbs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.github.jknack/handlebars "2.3.1"]]
  :profiles {:test {:dependencies [[ring "1.4.0"]]}}
  :plugins [[codox "0.8.10"]]
  :codox {:output-dir "target/codox"
          :exclude [hbs.ext]}
  :global-vars {*warn-on-reflection* true}
  :deploy-repositories {"releases" :clojars})
