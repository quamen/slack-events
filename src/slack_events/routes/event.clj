(ns slack-events.routes.event
  (:require [compojure.core :refer :all]
            [clj-http.client :as client]
            [environ.core :refer [env]]
            [clojure.data.json :as json]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(def slack-token
  (env :slack-token))

(def hook-url
  (env :slack-url))

(def meetup-api-key
  (env :meetup-api-key))

(defn pull-values [m val-map]
  (into {} (for [[k v] val-map]
             [k (get-in m (if (sequential? v) v [v]))])))

(defn meetup-for-group [group]
  (let [content (-> (str "https://api.meetup.com/"
                         group
                         "?sign=true&key="
                         meetup-api-key)
                    client/get
                    :body
                    json/read-str
                    (pull-values {:next-event-id ["next_event" "id"]}))]
    content))

(defn next-event [group]
  (let [content (-> (str "https://api.meetup.com/2/event/"
                         (:next-event-id (meetup-for-group group))
                         "?sign=true&key="
                         meetup-api-key)
                    client/get
                    :body
                    json/read-str
                    (pull-values {:name ["name"]
                                  :venue ["venue" "name"]
                                  :description ["description"]
                                  :time ["time"]
                                  :utc-offset ["utc_offset"]
                                  :event-url ["event_url"]
                                  }))]
    content))


(def date-time-format (f/formatter "d MMMM yyyy HH:mm"))

(defn event-time [e]
  (let [offset-time (+ (:time e) (:utc-offset e))]
    (f/unparse date-time-format (c/from-long offset-time))))

(defn event-to-str [e]
  (str (:name e) " at " (:venue e) " on " (event-time e) ", details: " (:event-url e)))

(defn post-to-slack [url msg]
  (let [m (merge {:username "Event Bot"
                  :icon_emoji ":zap:"} msg)]
    (client/post url {:body (json/write-str m)
                      :content-type :json})))

(defn next-event-to-slack [group]
  (let [event ( -> group
                next-event
                event-to-str)]
    (post-to-slack hook-url {:text event})))

(defn events [params]
  (if (and (= "/events" (:command params))
           (= slack-token (:token params)))
    (do
      (let [event ( -> (:text params)
                    next-event
                    event-to-str)]
        {:status 200
         :content-type "text/plain"
         :body event}))
    {:status 400
     :content-type "text/plain"
     :body "Nope."}))


(defroutes event-routes
  (POST "/events" {:keys [params] :as request} (events params)))
