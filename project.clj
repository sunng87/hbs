(defproject hbs "0.5.2-SNAPSHOT"
  :description "Clojure templating by Handlebars.java"
  :url "http://github.com/sunng87/hbs"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.github.jknack/handlebars "1.3.1"]]
  :java-source-paths ["java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :global-vars {*warn-on-reflection* true}
  :deploy-repositories {"releases" :clojars})
