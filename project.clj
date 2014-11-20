(defproject scratchbase "0.1.0-SNAPSHOT"
  :description "Scratchabase is a database toy."
  :url "http://github.com/mahinshaw/scratchbase"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [postgresql "9.3-1102.jdbc41"]
                 [ring/ring-jetty-adapter "1.2.1"]]
  :main ^:skip-aot scratchbase.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
