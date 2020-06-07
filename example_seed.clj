(ns example-seed
  (:require [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

(def ^:const seed-data
  [{:name "John Doe" :username "john.doe"}
   {:name "Stuart Sierra" :username "stuartsierra"}
   {:name "Rich Hickey" :username "richhickey"}])

(defn add-oid [doc]
  (assoc doc :_id (ObjectId.)))

(defn seed-db! [db]
  (let [data (map add-oid seed-data)]
    (doseq [user data]
      (mc/insert db :users user)))
  db)
