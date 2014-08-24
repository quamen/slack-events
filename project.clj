(defproject slack-events "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-http "1.0.0"]
                 [clj-time "0.8.0"]
                 [compojure "1.1.6"]
                 [environ "1.0.0"]
                 [ring-server "0.3.1"]]
  :plugins [[lein-environ "1.0.0"]
            [lein-ring "0.8.11"]]
  :ring {:handler slack-events.handler/app
         :init slack-events.handler/init
         :destroy slack-events.handler/destroy}
  :aot :all
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
