(defproject hbs "0.1.2"
  :description "Clojure templating by Handlebars.java"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.github.jknack/handlebars "0.7.0"]]
  :java-source-paths ["java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"])
