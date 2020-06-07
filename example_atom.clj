(ns example-atom
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [monger.core :as mg]
            [monger.collection :as mc]

            [example-seed :as seed))

(def database (atom nil))

(defn connect-db! [uri]
  (swap! database
         (fn [_] (-> uri
                     mg/connect-via-uri
                     :db
                     seed/seed-db!))))

(defn list-users! []
  (mc/find-maps @database :users))

(defn handler [request]
  {:status 200
   :body (clojure.string/join "\n" (list-users!))})

(defn -main
  [& args]
  (connect-db! (System/getenv "DATABASE_URL"))
  (run-jetty handler {:port 3000}))
