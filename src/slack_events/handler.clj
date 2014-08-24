(ns slack-events.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [slack-events.routes.event :refer [event-routes]]))

(defn init []
  (println "slack-events is starting"))

(defn destroy []
  (println "slack-events is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes event-routes app-routes)
      (handler/api)))
